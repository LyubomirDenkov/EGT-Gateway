package com.egt.gateway.services;

import com.egt.gateway.model.ExternalService;
import com.egt.gateway.model.RequestHistory;
import com.egt.gateway.repository.externalservices.ExternalServiceRepository;
import com.egt.gateway.repository.history.HistoryRepository;
import com.egt.gateway.service.statistic.HistoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HistoryServiceImplTests {

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private ExternalServiceRepository externalServiceRepository;
    @InjectMocks
    private HistoryServiceImpl historyService;

    private final String requestId = "123";
    private final String externalServiceName = "testService";
    private final String clientId = "client1";

    private ExternalService externalService;
    private RequestHistory requestHistory;

    @BeforeEach
    public void setUp() {
        externalService = ExternalService.builder().name(externalServiceName).build();
        requestHistory = RequestHistory.builder()
                .requestId(requestId)
                .client(clientId)
                .requestTime(LocalDateTime.now())
                .externalService(externalService)
                .build();
    }

    @Test
    public void testSaveRequestHistoryNewExternalService() {
        when(externalServiceRepository.findByServiceName(externalServiceName)).thenReturn(Optional.of(externalService));

        historyService.saveRequestHistory(requestId, externalServiceName, clientId);

        ArgumentCaptor<RequestHistory> requestHistoryCaptor = ArgumentCaptor.forClass(RequestHistory.class);
        verify(historyRepository).save(requestHistoryCaptor.capture());
        RequestHistory capturedRequestHistory = requestHistoryCaptor.getValue();
        assertEquals(requestId, capturedRequestHistory.getRequestId());
        assertEquals(clientId, capturedRequestHistory.getClient());
        assertNotNull(capturedRequestHistory.getRequestTime());
        assertEquals(externalServiceName, capturedRequestHistory.getExternalService().getName());
    }

    @Test
    public void testIsHistoryRequestIdExistTrue() {
        when(historyRepository.findByRequestId(requestId)).thenReturn(Optional.of(requestHistory));
        boolean exists = historyService.isHistoryRequestIdExist(requestId);
        assertTrue(exists);
    }

    @Test
    public void testIsHistoryRequestIdExistFalse() {
        when(historyRepository.findByRequestId(requestId)).thenReturn(Optional.empty());
        boolean exists = historyService.isHistoryRequestIdExist(requestId);
        assertFalse(exists);
    }
}
