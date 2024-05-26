package com.egt.gateway.controller.json.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RatesJsonData {
    private String updateTime;
    private Map<String, Double> rates;
}
