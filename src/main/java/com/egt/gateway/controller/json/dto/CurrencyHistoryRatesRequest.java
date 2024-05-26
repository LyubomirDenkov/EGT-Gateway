package com.egt.gateway.controller.json.dto;

import lombok.Data;

@Data
public class CurrencyHistoryRatesRequest {
    private String requestId;
    private Long timestamp;
    private String client;
    private String currency;
    private Long period;
}