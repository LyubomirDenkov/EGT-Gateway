package com.egt.gateway.client;

import com.egt.gateway.client.dto.RatesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FixerApiClientImpl implements FixerApiClient{

    private final RestTemplate restTemplate;
    private final String fixerUrl;
    private final String accessKey;


    @Autowired
    public FixerApiClientImpl(RestTemplate restTemplate, @Value("${fixer.url.latest}") String fixerUrl, @Value("${fixer.access.key}") String accessKey) {
        this.restTemplate = restTemplate;
        this.fixerUrl = fixerUrl;
        this.accessKey = accessKey;
    }

    @Override
    public RatesResponse collectRates() {
        return restTemplate.getForObject(buildUrl(), RatesResponse.class);
    }

    private String buildUrl() {
        return String.format("%s%s", fixerUrl, accessKey);
    }
}
