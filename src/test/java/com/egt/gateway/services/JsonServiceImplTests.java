package com.egt.gateway.services;

import com.egt.gateway.controller.json.dto.CurrentCurrencyRatesResponse;
import com.egt.gateway.controller.json.dto.RatesJsonData;
import com.egt.gateway.exceptions.DuplicateRequestException;
import com.egt.gateway.model.CurrencyData;
import com.egt.gateway.repository.currency.currencydata.CurrencyDataRepository;
import com.egt.gateway.service.json.JsonServiceImpl;
import com.egt.gateway.service.statistic.HistoryService;
import com.egt.gateway.service.util.RatesMappingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class JsonServiceImplTests {

    @Mock
    private CurrencyDataRepository currencyDataRepository;

    @Mock
    private RatesMappingService ratesMappingService;

    @Mock
    private HistoryService historyService;

    @InjectMocks
    private JsonServiceImpl jsonService;

    private CurrencyData mockCurrencyData;
    private static final String MOCK_REQUEST_ID = "mockRequestId";
    private static final String MOCK_CLIENT = "mockClient";
    private static final String MOCK_CURRENCY = "USD";

    @BeforeEach
    void setUp() {
        mockCurrencyData = CurrencyData.builder().build();
    }

    @Test
    void testGetCurrencyDataSuccess() {
        when(historyService.isHistoryRequestIdExist(MOCK_REQUEST_ID)).thenReturn(false);
        when(currencyDataRepository.findLatestByBaseCurrency(MOCK_CURRENCY)).thenReturn(Optional.of(mockCurrencyData));
        when(ratesMappingService.mapToResponseJsonRatesObject(any(CurrencyData.class)))
                .thenReturn(any());

        CurrentCurrencyRatesResponse response = jsonService.getCurrencyData(MOCK_REQUEST_ID, System.currentTimeMillis(), MOCK_CLIENT, MOCK_CURRENCY);

        assertNotNull(response);
        assertEquals(MOCK_CURRENCY, response.getBaseCurrency());
        assertEquals(1, response.getRates().size());
    }


    @Test
    void testGetCurrencyHistoryDataSuccess() {
        when(historyService.isHistoryRequestIdExist(MOCK_REQUEST_ID)).thenReturn(false);
        when(currencyDataRepository.findByBaseCurrencyAndPeriod(eq(MOCK_CURRENCY), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(mockCurrencyData));
        when(ratesMappingService.mapToResponseJsonRatesObject(any(CurrencyData.class)))
                .thenReturn(RatesJsonData.builder().build());

        CurrentCurrencyRatesResponse response = jsonService.getCurrencyHistoryData(MOCK_REQUEST_ID, System.currentTimeMillis(), MOCK_CLIENT, MOCK_CURRENCY, 24L);

        assertNotNull(response);
        assertEquals(MOCK_CURRENCY, response.getBaseCurrency());
        assertEquals(1, response.getRates().size());
    }

    @Test
    void testGetCurrencyHistoryDataDuplicateRequest() {
        when(historyService.isHistoryRequestIdExist(MOCK_REQUEST_ID)).thenReturn(true);

        DuplicateRequestException exception = assertThrows(DuplicateRequestException.class, () -> {
            jsonService.getCurrencyHistoryData(MOCK_REQUEST_ID, System.currentTimeMillis(), MOCK_CLIENT, MOCK_CURRENCY, 24L);
        });

        assertEquals(DuplicateRequestException.DUPLICATE_REQUEST_MESSAGE, exception.getMessage());
        verify(historyService, never()).saveRequestHistory(anyString(), anyString(), anyString());
    }
}
