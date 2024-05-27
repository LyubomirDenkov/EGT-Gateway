package com.egt.gateway.services;

import com.egt.gateway.controller.json.dto.CurrentCurrencyRatesResponse;
import com.egt.gateway.controller.xml.dto.CurrencyRatesXmlResponse;
import com.egt.gateway.controller.xml.dto.RatesXmlData;
import com.egt.gateway.exceptions.DuplicateRequestException;
import com.egt.gateway.model.CurrencyData;
import com.egt.gateway.repository.currency.currencydata.CurrencyDataRepository;
import com.egt.gateway.repository.history.HistoryRepository;
import com.egt.gateway.service.currency.util.RatesMappingService;
import com.egt.gateway.service.currency.util.UtilService;
import com.egt.gateway.service.currency.xml.XmlServiceImpl;
import com.egt.gateway.service.history.HistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class XmlServiceImplTests {

    @Mock
    private CurrencyDataRepository currencyDataRepository;

    @Mock
    private RatesMappingService ratesMappingService;

    @Mock
    private HistoryService historyService;

    @Mock
    private UtilService utilService;

    @Mock
    private HistoryRepository historyRepository;

    @InjectMocks
    private XmlServiceImpl xmlService;

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
        when(ratesMappingService.mapToResponseXmlRatesObject(any(CurrencyData.class)))
                .thenReturn(RatesXmlData.builder().build());
        doNothing().when(utilService).validateDataIsPresent(any());
        CurrencyRatesXmlResponse response = xmlService.getCurrencyData(MOCK_REQUEST_ID, MOCK_CLIENT, MOCK_CURRENCY);

        assertNotNull(response);
        assertEquals(MOCK_CURRENCY, response.getBaseCurrency());
    }


    @Test
    void testGetCurrencyDataDuplicateRequest() {
        doThrow(DuplicateRequestException.class).when(historyService).validateHistoryRequestIdNotExist(MOCK_REQUEST_ID);
        assertThrows(DuplicateRequestException.class, () -> {
            xmlService.getCurrencyData(MOCK_REQUEST_ID,  MOCK_CLIENT, MOCK_CURRENCY);
        });
    }

    @Test
    void testGetCurrencyHistoryDataDuplicateRequest() {
        doThrow(DuplicateRequestException.class).when(historyService).validateHistoryRequestIdNotExist(MOCK_REQUEST_ID);
        assertThrows(DuplicateRequestException.class, () -> {
            xmlService.getCurrencyHistoryData(MOCK_REQUEST_ID, MOCK_CLIENT, MOCK_CURRENCY, 24L);
        });
    }
}
