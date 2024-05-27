package com.egt.gateway.controller.xml;

import com.egt.gateway.controller.xml.dto.CommandRequest;
import com.egt.gateway.controller.xml.dto.CurrencyRatesXmlResponse;
import com.egt.gateway.controller.xml.dto.Get;
import com.egt.gateway.controller.xml.dto.History;
import com.egt.gateway.service.currency.xml.XmlService;
import jakarta.validation.Valid;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/xml_api")
@AllArgsConstructor
public class XmlController {

    private final XmlService xmlService;

    @PostMapping(value = "/command", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<CurrencyRatesXmlResponse> handleCommand(@RequestBody String xml) {
        try {
            JAXBContext context = JAXBContext.newInstance(CommandRequest.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            CommandRequest command = (CommandRequest) unmarshaller.unmarshal(new StringReader(xml));
            Optional<Get> get = Optional.ofNullable(command.getGet());
            Optional<History> history = Optional.ofNullable(command.getHistory());
            if (get.isPresent()) {
                return ResponseEntity.ok(xmlService.getCurrencyData(command.getId(), get.get().getConsumer(), get.get().getCurrency()));
            } else {
                return ResponseEntity.ok(xmlService.getCurrencyHistoryData(command.getId(), history.get().getConsumer(), history.get().getCurrency(), history.get().getPeriod()));
            }
        } catch (JAXBException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
