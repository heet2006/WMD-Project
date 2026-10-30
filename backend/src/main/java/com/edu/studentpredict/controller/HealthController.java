package com.edu.studentpredict.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@Tag(name = "System Health", description = "Liveness and Readiness probe")
public class HealthController {

    @GetMapping
    @Operation(summary = "Check backend service health and SDG 4 status")
    public ResponseEntity<Map<String, Object>> checkHealth() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "Student Academic Performance Prediction API");
        status.put("timestamp", LocalDateTime.now());
        status.put("sdgAlignment", "SDG 4 - Quality Education");
        status.put("experimentsIntegrated", "1 through 11");
        return ResponseEntity.ok(status);
    }
}
