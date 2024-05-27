package com.egt.gateway.service.ratescollector.rates;

import java.util.Map;

public interface RatesService {
    void updateRatedValues(String baseCurrencyCode, String ratesJsonData);
}
