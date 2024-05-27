package com.egt.gateway.service.currency.util;

import com.egt.gateway.controller.json.dto.RatesJsonData;
import com.egt.gateway.controller.xml.dto.RatesXmlData;
import com.egt.gateway.model.CurrencyData;

public interface RatesMappingService {
    RatesJsonData mapToResponseJsonRatesObject(CurrencyData currencyData);
    RatesXmlData mapToResponseXmlRatesObject(CurrencyData currencyData);
}
