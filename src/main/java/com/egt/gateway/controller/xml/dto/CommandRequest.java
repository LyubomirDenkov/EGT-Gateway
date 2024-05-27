package com.egt.gateway.controller.xml.dto;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

@XmlRootElement(name = "command")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class CommandRequest {

    @XmlAttribute(name = "id")
    private String id;

    @XmlElement(name = "get")
    private Get get;

    @XmlElement(name = "history")
    private History history;

}

