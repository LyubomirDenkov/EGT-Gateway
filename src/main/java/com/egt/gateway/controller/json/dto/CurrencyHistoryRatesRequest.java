package com.egt.gateway.controller.json.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CurrencyHistoryRatesRequest {
    @NotBlank
    private String requestId;
    @NotNull
    @Positive
    private Long timestamp;
    @NotBlank
    private String client;
    @NotBlank
    private String currency;
    @NotNull
    @Positive
    private Long period;
}