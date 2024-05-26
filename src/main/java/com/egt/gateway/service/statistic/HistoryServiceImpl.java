package com.egt.gateway.service.statistic;

import com.egt.gateway.model.ExternalService;
import com.egt.gateway.model.RequestHistory;
import com.egt.gateway.repository.externalservices.ExternalServiceRepository;
import com.egt.gateway.repository.history.HistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;
    private final ExternalServiceRepository externalServiceRepository;

    @Override
    public void saveRequestHistory(String requestId, String externalServiceName, String clientId) {
        Optional<ExternalService> externalService = externalServiceRepository.findByServiceName(externalServiceName);
        if (!externalService.isPresent()) {
            externalService = Optional.of(externalServiceRepository.save(ExternalService.builder()
                    .name(externalServiceName)
                    .build()));
        }
        historyRepository.save(RequestHistory.builder()
                .requestId(requestId)
                .client(clientId)
                .requestTime(LocalDateTime.now())
                .externalService(externalService.get())
                .build());
    }

    @Override
    public boolean isHistoryRequestIdExist(String requestId) {
        Optional<RequestHistory> requestHistory = historyRepository.findByRequestId(requestId);
        return requestHistory.isPresent();
    }

}
