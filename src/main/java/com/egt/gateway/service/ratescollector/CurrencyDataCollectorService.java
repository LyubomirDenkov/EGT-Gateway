package com.egt.gateway.service.ratescollector;

import com.egt.gateway.client.FixerApiClient;
import com.egt.gateway.client.dto.RatesResponse;
import com.egt.gateway.repository.currency.currencycode.CurrencyCodeRepository;
import com.egt.gateway.service.ratescollector.currencycodes.CurrencyCodeInitService;
import com.egt.gateway.service.rabbit.RabbitService;
import com.egt.gateway.service.ratescollector.rates.RatesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
public class CurrencyDataCollectorService {
    private static final String TRANSFORM_ERROR_MESSAGE = "Fixer rates transform to JSON fail!";
    private static final String RATES_DATA_FETCHED = "Fixer rates data fetched";
    private static final long FETCH_DATA_TIME_FRAME = 600000L;

    private final FixerApiClient fixerApiClient;
    private final CurrencyCodeRepository currencyCodeRepository;
    private final CurrencyCodeInitService currencyCodeInitService;
    private final RatesService ratesService;
    private final ObjectMapper objectMapper;
    private final RabbitService rabbitService;


    @Autowired
    public CurrencyDataCollectorService(FixerApiClient fixerApiClient,
                                        CurrencyCodeRepository currencyCodeRepository,
                                        CurrencyCodeInitService currencyCodeInitService,
                                        RatesService ratesService,
                                        ObjectMapper objectMapper,
                                        RabbitService rabbitService) {
        this.fixerApiClient = fixerApiClient;
        this.currencyCodeRepository = currencyCodeRepository;
        this.currencyCodeInitService = currencyCodeInitService;
        this.ratesService = ratesService;
        this.objectMapper = objectMapper;
        this.rabbitService = rabbitService;
    }

    @Scheduled(fixedDelay = FETCH_DATA_TIME_FRAME)
    public void run() {
        RatesResponse ratesResponse = fixerApiClient.collectRates();
        Long existingCurrenciesCount = currencyCodeRepository.countAll();
        if (existingCurrenciesCount != ratesResponse.getRates().size()) {
            currencyCodeInitService.fillCurrencyCodeData(new ArrayList<>(ratesResponse.getRates().keySet()));
        }
        Optional<String> ratesJson;
        try {
            ratesJson = Optional.ofNullable(objectMapper.writeValueAsString(ratesResponse.getRates()));
        } catch (JsonProcessingException e) {
            log.error("{}", TRANSFORM_ERROR_MESSAGE);
            throw new RuntimeException(TRANSFORM_ERROR_MESSAGE);
        }
        ratesJson.ifPresent(c -> {
            ratesService.updateRatedValues(ratesResponse.getBase(), c);
            rabbitService.sendMessageToWebSocketClient();
            log.info("{}", RATES_DATA_FETCHED);
        });
    }
}
