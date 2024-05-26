package com.egt.gateway.service.ratescollector.rates;

public interface RatesService {
    void updateRatedValues(String baseCurrencyCode, String ratesJsonData);
}
