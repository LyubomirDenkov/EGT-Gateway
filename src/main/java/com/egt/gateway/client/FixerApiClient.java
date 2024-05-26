package com.egt.gateway.client;

import com.egt.gateway.model.dto.RatesResponse;

public interface FixerApiClient {

    RatesResponse collectRates();
}
