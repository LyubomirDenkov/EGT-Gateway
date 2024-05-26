package com.egt.gateway.controller.xml.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Get {

    @XmlAttribute(name = "consumer")
    private String consumer;

    @XmlElement(name = "currency")
    private String currency;

}
