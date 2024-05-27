package com.egt.gateway.service.currency.xml;

import com.egt.gateway.controller.xml.dto.CurrencyRatesXmlResponse;

public interface XmlService {
    CurrencyRatesXmlResponse getCurrencyData(String requestId, String client, String currency);

    CurrencyRatesXmlResponse getCurrencyHistoryData(String requestId, String client, String currency, Long period);
}
