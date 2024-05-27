package com.egt.gateway.service.history;

import com.egt.gateway.controller.externalservice.dto.RequestsExternalServicesResponse;

import java.time.LocalDateTime;

public interface HistoryService {

    void saveRequestHistory(String requestId, LocalDateTime time, String externalServiceName, String clientId);

    void validateHistoryRequestIdNotExist(String requestId);

    RequestsExternalServicesResponse getRequestCountInTimeFrame(String externalServiceId, Long timeRangeStart, Long timeRangeEnd);
}
