package com.egt.gateway.controller.json;

import com.egt.gateway.controller.json.dto.CurrencyHistoryRatesRequest;
import com.egt.gateway.controller.json.dto.CurrentCurrencyRatesRequest;
import com.egt.gateway.controller.json.dto.CurrentCurrencyRatesResponse;
import com.egt.gateway.service.currency.json.JsonService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/json_api")
@AllArgsConstructor
public class JsonController {

    private final JsonService jsonService;

    @PostMapping(value = "/current", produces = "application/json")
    public ResponseEntity<CurrentCurrencyRatesResponse> currencyRequest(@Valid @RequestBody CurrentCurrencyRatesRequest request) {
        return ResponseEntity.ok(
                jsonService.getCurrencyData(request.getRequestId(), request.getTimestamp(), request.getClient(), request.getCurrency()));
    }

    @PostMapping("/history")
    public ResponseEntity<CurrentCurrencyRatesResponse> historyRequest(@Valid @RequestBody CurrencyHistoryRatesRequest request) {
        return ResponseEntity.ok(jsonService.getCurrencyHistoryData(request.getRequestId(), request.getTimestamp(), request.getClient(), request.getCurrency(), request.getPeriod()));
    }
}
