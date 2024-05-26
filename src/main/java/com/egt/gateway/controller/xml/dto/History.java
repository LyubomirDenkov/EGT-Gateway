package com.egt.gateway.controller.xml.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class History {

    @XmlAttribute(name = "consumer")
    private String consumer;

    @XmlAttribute(name = "currency")
    private String currency;

    @XmlAttribute(name = "period")
    private long period;
}
