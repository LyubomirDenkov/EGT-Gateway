package com.egt.gateway.service.statistic;

public interface HistoryService {

    void saveRequestHistory(String requestId, String externalServiceName, String clientId);

    boolean isHistoryRequestIdExist(String requestId);
}
