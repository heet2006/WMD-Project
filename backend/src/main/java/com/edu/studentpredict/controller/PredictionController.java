package com.edu.studentpredict.controller;

import com.edu.studentpredict.dto.PredictionRequestDto;
import com.edu.studentpredict.dto.PredictionResponseDto;
import com.edu.studentpredict.service.PredictionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Decision Tree Machine Learning Prediction", description = "Endpoints for student performance inference and model metrics")
public class PredictionController {

    private final PredictionService predictionService;

    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @PostMapping("/predict")
    @Operation(summary = "Perform single student performance prediction with explainability and recommendations")
    public ResponseEntity<PredictionResponseDto> predictPerformance(@Valid @RequestBody PredictionRequestDto request) {
        PredictionResponseDto response = predictionService.predictPerformance(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/predict/batch")
    @Operation(summary = "Perform batch prediction for multiple students")
    public ResponseEntity<Map<String, Object>> predictBatch(@RequestBody List<PredictionRequestDto> students) {
        List<PredictionResponseDto> results = new ArrayList<>();
        for (PredictionRequestDto student : students) {
            results.add(predictionService.predictPerformance(student));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("total", results.size());
        response.put("predictions", results);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/model/metrics")
    @Operation(summary = "Retrieve Decision Tree evaluation metrics (accuracy, precision, recall, F1, confusion matrix)")
    public ResponseEntity<Map<String, Object>> getModelMetrics() {
        return ResponseEntity.ok(predictionService.getModelMetrics());
    }
}
