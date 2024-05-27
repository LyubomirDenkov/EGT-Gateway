package com.egt.gateway.controller.externalservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RequestsExternalServicesRequest {
    @NotBlank
    private String serviceId;
    @NotNull
    @Positive
    private long timeRangeStart;
    @NotNull
    @Positive
    private long timeRangeEnd;
}
