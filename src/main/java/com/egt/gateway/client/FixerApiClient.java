package com.egt.gateway.client;

import com.egt.gateway.client.dto.RatesResponse;

public interface FixerApiClient {

    RatesResponse collectRates();
}
