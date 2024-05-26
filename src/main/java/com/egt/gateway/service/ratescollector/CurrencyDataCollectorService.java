package com.egt.gateway.service.ratescollector;

import com.egt.gateway.client.FixerApiClient;
import com.egt.gateway.model.dto.RatesResponse;
import com.egt.gateway.repository.currency.currencycode.CurrencyCodeRepository;
import com.egt.gateway.service.ratescollector.currencycodes.CurrencyCodeInitService;
import com.egt.gateway.service.rabbit.RabbitService;
import com.egt.gateway.service.ratescollector.rates.RatesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CurrencyDataCollectorService {
    private static final String TRANSFORM_ERROR_MESSAGE = "Fixer rates transform to JSON fail!";

    private final FixerApiClient fixerApiClient;
    private final CurrencyCodeRepository currencyCodeRepository;
    private final CurrencyCodeInitService currencyCodeInitService;
    private final RatesService ratesService;
    private final ObjectMapper objectMapper;
    private final RabbitService rabbitService;

    @Scheduled(fixedDelay = 600000)
    public void run() {
        RatesResponse ratesResponse = fixerApiClient.collectRates();
        Long existingCurrenciesCount = currencyCodeRepository.countAll();
        if (existingCurrenciesCount != ratesResponse.getRates().size()) {
            currencyCodeInitService.fillCurrencyCodeData(new ArrayList<>(ratesResponse.getRates().keySet()));
        }
        Optional<String> ratesJson = Optional.empty();
        try {
            ratesJson = Optional.ofNullable(objectMapper.writeValueAsString(ratesResponse.getRates()));
        } catch (JsonProcessingException e) {
            log.error("{}", TRANSFORM_ERROR_MESSAGE);
        }
        ratesJson.ifPresent(c -> {
            ratesService.updateRatedValues(ratesResponse.getBase(), c);
            rabbitService.sendMessageToWebSocketClient();
        });
    }
}
