package com.egt.gateway.services;

import com.egt.gateway.controller.externalservice.dto.RequestsExternalServicesResponse;
import com.egt.gateway.exceptions.DuplicateRequestException;
import com.egt.gateway.exceptions.TimeRangeException;
import com.egt.gateway.model.ExternalService;
import com.egt.gateway.model.RequestHistory;
import com.egt.gateway.repository.externalservices.ExternalServiceRepository;
import com.egt.gateway.repository.history.HistoryRepository;
import com.egt.gateway.service.history.HistoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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

        historyService.saveRequestHistory(requestId, LocalDateTime.now(), externalServiceName, clientId);

        ArgumentCaptor<RequestHistory> requestHistoryCaptor = ArgumentCaptor.forClass(RequestHistory.class);
        verify(historyRepository).save(requestHistoryCaptor.capture());
        RequestHistory capturedRequestHistory = requestHistoryCaptor.getValue();
        assertEquals(requestId, capturedRequestHistory.getRequestId());
        assertEquals(clientId, capturedRequestHistory.getClient());
        assertNotNull(capturedRequestHistory.getRequestTime());
        assertEquals(externalServiceName, capturedRequestHistory.getExternalService().getName());
    }

    @Test
    public void testIsHistoryRequestIdNotExistFalse() {
        when(historyRepository.findByRequestId(requestId)).thenReturn(Optional.empty());

        historyService.validateHistoryRequestIdNotExist(requestId);

        verify(historyRepository, times(1)).findByRequestId(requestId);
    }

    @Test
    public void testIsHistoryRequestIdExist() {
        when(historyRepository.findByRequestId(requestId)).thenReturn(Optional.of(RequestHistory.builder().build()));

        assertThrows(DuplicateRequestException.class, () -> {
            historyService.validateHistoryRequestIdNotExist(requestId);
        });

        verify(historyRepository, times(1)).findByRequestId(requestId);
    }

    @Test
    public void testValidateTimeFrameRangeThrowForDays() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(6);
        LocalDateTime endTime = LocalDateTime.now();
        long startTimeMock = startTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        long endTimeMock = endTime.toInstant(ZoneOffset.UTC).toEpochMilli();

        assertThrows(TimeRangeException.class, () -> {
            historyService.getRequestCountInTimeFrame(externalServiceName,startTimeMock, endTimeMock);
        });
    }

    @Test
    public void testValidateTimeFrameRangeThrowForHours() {
        LocalDateTime startTime = LocalDateTime.now().minusHours(27);
        LocalDateTime endTime = LocalDateTime.now();
        long startTimeMock = startTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        long endTimeMock = endTime.toInstant(ZoneOffset.UTC).toEpochMilli();

        assertThrows(TimeRangeException.class, () -> {
            historyService.getRequestCountInTimeFrame(externalServiceName,startTimeMock, endTimeMock);
        });
    }
}
