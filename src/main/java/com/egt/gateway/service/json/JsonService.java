package com.egt.gateway.service.json;

import com.egt.gateway.controller.json.dto.CurrentCurrencyRatesResponse;

import java.util.List;

public interface JsonService {
    CurrentCurrencyRatesResponse getCurrencyData(String requestId, Long timestamp, String client, String currency);

    CurrentCurrencyRatesResponse getCurrencyHistoryData(String requestId, Long timestamp, String client, String currency, Long period);
}
