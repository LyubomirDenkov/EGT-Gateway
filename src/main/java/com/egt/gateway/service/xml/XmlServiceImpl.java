package com.egt.gateway.service.xml;

import com.egt.gateway.controller.xml.dto.CurrencyRatesXmlResponse;
import com.egt.gateway.exceptions.DuplicateRequestException;
import com.egt.gateway.model.CurrencyData;
import com.egt.gateway.repository.currency.currencydata.CurrencyDataRepository;
import com.egt.gateway.service.statistic.HistoryService;
import com.egt.gateway.service.util.RatesMappingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.egt.gateway.exceptions.DuplicateRequestException.DUPLICATE_REQUEST_MESSAGE;

@Service
@AllArgsConstructor
public class XmlServiceImpl implements XmlService {

    private static final String EXTERNAL_SERVICE_2 = "SERVICE_TWO";

    private final CurrencyDataRepository currencyDataRepository;
    private final RatesMappingService ratesMappingService;
    private final HistoryService historyService;

    @Override
    public CurrencyRatesXmlResponse getCurrencyData(String requestId, String client, String currency) {
        validateUniqueRequestId(requestId);
        historyService.saveRequestHistory(requestId, EXTERNAL_SERVICE_2, client);
        Optional<CurrencyData> currencyData = currencyDataRepository.findLatestByBaseCurrency(currency);
        return CurrencyRatesXmlResponse.builder()
                .baseCurrency(currency)
                .ratesData(Collections.singletonList(ratesMappingService.mapToResponseXmlRatesObject(currencyData.get())))
                .build();
    }

    @Override
    public CurrencyRatesXmlResponse getCurrencyHistoryData(String requestId, String client, String currency, Long period) {
        validateUniqueRequestId(requestId);
        historyService.saveRequestHistory(requestId, EXTERNAL_SERVICE_2, client);
        LocalDateTime ratesFromDate = LocalDateTime.now().minusHours(period);
        List<CurrencyData> currencyHistoryData = currencyDataRepository.findByBaseCurrencyAndPeriod(currency, ratesFromDate);
        return CurrencyRatesXmlResponse.builder()
                .baseCurrency(currency)
                .ratesData(currencyHistoryData.stream()
                        .map(ratesMappingService::mapToResponseXmlRatesObject)
                        .collect(Collectors.toList()))
                .build();
    }

    private void validateUniqueRequestId(String requestId) {
        if (historyService.isHistoryRequestIdExist(requestId)) {
            throw new DuplicateRequestException(DUPLICATE_REQUEST_MESSAGE);
        }
    }
}
