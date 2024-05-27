package com.egt.gateway.service.history;

import com.egt.gateway.controller.externalservice.dto.RequestsExternalServicesResponse;
import com.egt.gateway.exceptions.DuplicateRequestException;
import com.egt.gateway.exceptions.TimeRangeException;
import com.egt.gateway.model.ExternalService;
import com.egt.gateway.model.RequestHistory;
import com.egt.gateway.repository.externalservices.ExternalServiceRepository;
import com.egt.gateway.repository.history.HistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static com.egt.gateway.exceptions.DuplicateRequestException.DUPLICATE_REQUEST_MESSAGE;

@Service
@AllArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    private static final String TIME_RANGE_ERROR_MESSAGE = "NOT GREATER THAN 24 HOURS AND NOT OLDER THAN 5 DAYS.";

    private final HistoryRepository historyRepository;
    private final ExternalServiceRepository externalServiceRepository;

    @Override
    public void saveRequestHistory(String requestId,LocalDateTime time, String externalServiceName, String clientId) {
        Optional<ExternalService> externalService = externalServiceRepository.findByServiceName(externalServiceName);
        if (!externalService.isPresent()) {
            externalService = Optional.of(externalServiceRepository.save(ExternalService.builder()
                    .name(externalServiceName)
                    .build()));
        }
        historyRepository.save(RequestHistory.builder()
                .requestId(requestId)
                .client(clientId)
                .requestTime(time)
                .externalService(externalService.get())
                .build());
    }

    @Override
    public void validateHistoryRequestIdNotExist(String requestId) {
        Optional<RequestHistory> requestHistory = historyRepository.findByRequestId(requestId);
        if (requestHistory.isPresent()) {
            throw new DuplicateRequestException(DUPLICATE_REQUEST_MESSAGE);
        }
    }

    @Override
    public RequestsExternalServicesResponse getRequestCountInTimeFrame(String externalServiceId, Long timeRangeStart, Long timeRangeEnd) {
        LocalDateTime startTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeRangeStart), ZoneOffset.UTC);
        LocalDateTime endTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeRangeEnd), ZoneOffset.UTC);
        validateTimeFrameRange(startTime, endTime);
        return RequestsExternalServicesResponse.builder()
                .requests(historyRepository.countByServiceIdAndTimestampBetween(externalServiceId, startTime, endTime))
                .build();
    }

    private void validateTimeFrameRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.plusHours(24).isBefore(endTime)) {
            throw new TimeRangeException(TIME_RANGE_ERROR_MESSAGE);
        }
        if (startTime.isAfter(endTime)) {
            throw new TimeRangeException(TIME_RANGE_ERROR_MESSAGE);
        }
        if (startTime.isBefore(LocalDateTime.now().minusDays(5))) {
            throw new TimeRangeException(TIME_RANGE_ERROR_MESSAGE);
        }
    }
}
