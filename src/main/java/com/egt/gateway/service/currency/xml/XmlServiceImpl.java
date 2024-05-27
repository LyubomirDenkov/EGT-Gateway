package com.egt.gateway.service.currency.xml;

import com.egt.gateway.controller.xml.dto.CurrencyRatesXmlResponse;
import com.egt.gateway.model.CurrencyData;
import com.egt.gateway.repository.currency.currencydata.CurrencyDataRepository;
import com.egt.gateway.service.currency.util.UtilService;
import com.egt.gateway.service.history.HistoryService;
import com.egt.gateway.service.currency.util.RatesMappingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class XmlServiceImpl implements XmlService {

    private static final String EXTERNAL_SERVICE_2 = "SERVICE_TWO";

    private final CurrencyDataRepository currencyDataRepository;
    private final RatesMappingService ratesMappingService;
    private final HistoryService historyService;
    private final UtilService utilService;

    @Override
    public CurrencyRatesXmlResponse getCurrencyData(String requestId, String client, String currency) {
        utilService.validateDataIsPresent(requestId);
        utilService.validateDataIsPresent(client);
        utilService.validateDataIsPresent(currency);
        historyService.validateHistoryRequestIdNotExist(requestId);
        historyService.saveRequestHistory(requestId, LocalDateTime.now(), EXTERNAL_SERVICE_2, client);
        Optional<CurrencyData> currencyData = currencyDataRepository.findLatestByBaseCurrency(currency);
        return CurrencyRatesXmlResponse.builder()
                .baseCurrency(currency)
                .ratesData(Collections.singletonList(ratesMappingService.mapToResponseXmlRatesObject(currencyData.get())))
                .build();
    }

    @Override
    public CurrencyRatesXmlResponse getCurrencyHistoryData(String requestId, String client, String currency, Long period) {
        utilService.validateDataIsPresent(requestId);
        utilService.validateDataIsPresent(client);
        utilService.validateDataIsPresent(currency);
        utilService.validateDataIsPresent(period);
        historyService.validateHistoryRequestIdNotExist(requestId);
        historyService.saveRequestHistory(requestId, LocalDateTime.now(), EXTERNAL_SERVICE_2, client);
        LocalDateTime ratesFromDate = LocalDateTime.now().minusHours(period);
        List<CurrencyData> currencyHistoryData = currencyDataRepository.findByBaseCurrencyAndPeriod(currency, ratesFromDate);
        return CurrencyRatesXmlResponse.builder()
                .baseCurrency(currency)
                .ratesData(currencyHistoryData.stream()
                        .map(ratesMappingService::mapToResponseXmlRatesObject)
                        .collect(Collectors.toList()))
                .build();
    }

}
