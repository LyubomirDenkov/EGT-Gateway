package com.egt.gateway.controller.xml.dto;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@XmlRootElement(name = "CurrencyRatesXmlResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CurrencyRatesXmlResponse {

    @XmlElement(name = "BaseCurrency")
    private String baseCurrency;

    @XmlElement(name = "RatesXmlData")
    private List<RatesXmlData> ratesData;
}