package com.egt.gateway.service.currency.json;

import com.egt.gateway.controller.json.dto.CurrentCurrencyRatesResponse;
import com.egt.gateway.model.CurrencyData;
import com.egt.gateway.repository.currency.currencydata.CurrencyDataRepository;
import com.egt.gateway.service.currency.util.UtilService;
import com.egt.gateway.service.history.HistoryService;
import com.egt.gateway.service.currency.util.RatesMappingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;

@Service
@AllArgsConstructor
public class JsonServiceImpl implements JsonService {
    private static final String EXTERNAL_SERVICE_1 = "SERVICE_ONE";

    private final CurrencyDataRepository currencyDataRepository;
    private final RatesMappingService ratesMappingService;
    private final HistoryService historyService;

    @Override
    public CurrentCurrencyRatesResponse getCurrencyData(String requestId, Long timestamp, String client, String currency) {
        historyService.validateHistoryRequestIdNotExist(requestId);
        historyService.saveRequestHistory(requestId, LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), UTC), EXTERNAL_SERVICE_1, client);
        Optional<CurrencyData> currencyData = currencyDataRepository.findLatestByBaseCurrency(currency);
        return CurrentCurrencyRatesResponse.builder()
                .baseCurrency(currency)
                .rates(Collections.singletonList(ratesMappingService.mapToResponseJsonRatesObject(currencyData.get())))
                .build();
    }

    @Override
    public CurrentCurrencyRatesResponse getCurrencyHistoryData(String requestId, Long timestamp, String client, String currency, Long period) {
        historyService.validateHistoryRequestIdNotExist(requestId);
        historyService.saveRequestHistory(requestId, LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), UTC), EXTERNAL_SERVICE_1, client);
        LocalDateTime ratesFromDate = LocalDateTime.now().minusHours(period);
        List<CurrencyData> currencyHistoryData = currencyDataRepository.findByBaseCurrencyAndPeriod(currency, ratesFromDate);
        return CurrentCurrencyRatesResponse.builder()
                .baseCurrency(currency)
                .rates(currencyHistoryData.stream()
                        .map(ratesMappingService::mapToResponseJsonRatesObject)
                        .collect(Collectors.toList()))
                .build();
    }
}
