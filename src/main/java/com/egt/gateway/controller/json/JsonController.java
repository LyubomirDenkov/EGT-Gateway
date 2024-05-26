package com.egt.gateway.controller.json;

import com.egt.gateway.controller.json.dto.CurrencyHistoryRatesRequest;
import com.egt.gateway.controller.json.dto.CurrentCurrencyRatesRequest;
import com.egt.gateway.controller.json.dto.CurrentCurrencyRatesResponse;
import com.egt.gateway.exceptions.DuplicateRequestException;
import com.egt.gateway.service.json.JsonService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/json_api")
@AllArgsConstructor
public class JsonController {

    private final JsonService jsonService;

    @PostMapping(value = "/current", produces = "application/json")
    public ResponseEntity<CurrentCurrencyRatesResponse> currencyRequest(@RequestBody CurrentCurrencyRatesRequest request) {
        return ResponseEntity.ok(
                jsonService.getCurrencyData(request.getRequestId(), request.getTimestamp(), request.getClient(), request.getCurrency()));
    }

    @PostMapping("/history")
    public ResponseEntity<CurrentCurrencyRatesResponse> historyRequest(@RequestBody CurrencyHistoryRatesRequest request) {
        return ResponseEntity.ok(jsonService.getCurrencyHistoryData(request.getRequestId(), request.getTimestamp(), request.getClient(), request.getCurrency(), request.getPeriod()));
    }

    @ExceptionHandler
    public ResponseEntity<String> handleDuplicateRequestException(DuplicateRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
