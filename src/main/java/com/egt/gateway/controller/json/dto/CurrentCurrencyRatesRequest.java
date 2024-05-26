package com.egt.gateway.controller.json.dto;

import lombok.Data;

@Data
public class CurrentCurrencyRatesRequest {
    private String requestId;
    private Long timestamp;
    private String client;
    private String currency;
}