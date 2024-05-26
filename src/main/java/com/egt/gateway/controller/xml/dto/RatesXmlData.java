package com.egt.gateway.controller.xml.dto;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@XmlRootElement(name = "RatesXmlData")
@XmlAccessorType(XmlAccessType.FIELD)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RatesXmlData {

    @XmlElement(name = "UpdateTime")
    private String updateTime;

    @XmlElementWrapper(name = "Rates")
    @XmlElement(name = "Rate")
    private Map<String, Double> rates;
}
