package com.egt.gateway.services;

import com.egt.gateway.controller.json.dto.CurrentCurrencyRatesResponse;
import com.egt.gateway.controller.json.dto.RatesJsonData;
import com.egt.gateway.exceptions.DuplicateRequestException;
import com.egt.gateway.model.CurrencyData;
import com.egt.gateway.model.RequestHistory;
import com.egt.gateway.repository.currency.currencydata.CurrencyDataRepository;
import com.egt.gateway.repository.history.HistoryRepository;
import com.egt.gateway.service.currency.json.JsonServiceImpl;
import com.egt.gateway.service.history.HistoryService;
import com.egt.gateway.service.currency.util.RatesMappingService;
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

    @Mock
    private HistoryRepository historyRepository;

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
        when(currencyDataRepository.findLatestByBaseCurrency(MOCK_CURRENCY)).thenReturn(Optional.of(mockCurrencyData));
        when(ratesMappingService.mapToResponseJsonRatesObject(any(CurrencyData.class)))
                .thenReturn(any());

        CurrentCurrencyRatesResponse response = jsonService.getCurrencyData(MOCK_REQUEST_ID, System.currentTimeMillis(), MOCK_CLIENT, MOCK_CURRENCY);

        assertNotNull(response);
        assertEquals(MOCK_CURRENCY, response.getBaseCurrency());
        assertEquals(1, response.getRates().size());
    }


    @Test
    void testGetCurrencyDataDuplicateRequest() {
        doThrow(DuplicateRequestException.class).when(historyService).validateHistoryRequestIdNotExist(MOCK_REQUEST_ID);
        DuplicateRequestException exception = assertThrows(DuplicateRequestException.class, () -> {
            jsonService.getCurrencyData(MOCK_REQUEST_ID, System.currentTimeMillis(), MOCK_CLIENT, MOCK_CURRENCY);
        });
    }

    @Test
    void testGetCurrencyHistoryDataDuplicateRequest() {
        doThrow(DuplicateRequestException.class).when(historyService).validateHistoryRequestIdNotExist(MOCK_REQUEST_ID);
        DuplicateRequestException exception = assertThrows(DuplicateRequestException.class, () -> {
            jsonService.getCurrencyHistoryData(MOCK_REQUEST_ID, System.currentTimeMillis(), MOCK_CLIENT, MOCK_CURRENCY, 24L);
        });
    }
}
