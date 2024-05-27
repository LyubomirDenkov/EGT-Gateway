package com.egt.gateway.controller.externalservice;

import com.egt.gateway.controller.externalservice.dto.RequestsExternalServicesRequest;
import com.egt.gateway.controller.externalservice.dto.RequestsExternalServicesResponse;
import com.egt.gateway.exceptions.TimeRangeException;
import com.egt.gateway.service.history.HistoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/requests")
@AllArgsConstructor
public class ExternalServiceController {

    private final HistoryService historyService;

    @PostMapping("/count")
    public ResponseEntity<RequestsExternalServicesResponse> getRequestHistory(@Valid @RequestBody RequestsExternalServicesRequest request) {
        return ResponseEntity.ok(historyService.getRequestCountInTimeFrame(request.getServiceId(), request.getTimeRangeStart(), request.getTimeRangeEnd()));
    }

}
