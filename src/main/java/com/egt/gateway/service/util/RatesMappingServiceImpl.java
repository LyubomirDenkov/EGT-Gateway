package com.egt.gateway.service.util;

import com.egt.gateway.controller.json.dto.CurrentCurrencyRatesResponse;
import com.egt.gateway.controller.json.dto.RatesJsonData;
import com.egt.gateway.controller.xml.dto.RatesXmlData;
import com.egt.gateway.model.CurrencyData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RatesMappingServiceImpl implements RatesMappingService {

    private final ObjectMapper objectMapper;

    @Override
    public RatesJsonData mapToResponseJsonRatesObject(CurrencyData currencyData) {
        try {
            return RatesJsonData.builder()
                    .updateTime(currencyData.getUpdatedTime().toString())
                    .rates(objectMapper.readValue(currencyData.getCurrencyRates(), new TypeReference<>() {
                    }))
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RatesXmlData mapToResponseXmlRatesObject(CurrencyData currencyData) {
        try {
            return RatesXmlData.builder()
                    .updateTime(currencyData.getUpdatedTime().toString())
                    .rates(objectMapper.readValue(currencyData.getCurrencyRates(), new TypeReference<>() {
                    }))
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
