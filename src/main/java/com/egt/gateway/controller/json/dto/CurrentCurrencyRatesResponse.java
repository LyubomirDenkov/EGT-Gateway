package com.egt.gateway.controller.json.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CurrentCurrencyRatesResponse {
    private String baseCurrency;
    private List<RatesJsonData> rates;
}
