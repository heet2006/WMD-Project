package com.edu.studentpredict.dto;

import com.edu.studentpredict.model.PerformanceLevel;
import java.util.List;
import java.util.Map;

public class PredictionResponseDto {

    private String studentId;
    private String name;
    private PerformanceLevel predictedPerformance;
    private Double confidence;
    private Map<String, Double> probabilities;
    private List<String> decisionPath;
    private List<String> contributingFactors;
    private List<String> academicRecommendations;
    private boolean isAtRisk;

    public PredictionResponseDto() {}

    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public PerformanceLevel getPredictedPerformance() { return predictedPerformance; }
    public void setPredictedPerformance(PerformanceLevel predictedPerformance) { this.predictedPerformance = predictedPerformance; }

    public Double getConfidence() { return confidence; }
    public void setConfidence(Double confidence) { this.confidence = confidence; }

    public Map<String, Double> getProbabilities() { return probabilities; }
    public void setProbabilities(Map<String, Double> probabilities) { this.probabilities = probabilities; }

    public List<String> getDecisionPath() { return decisionPath; }
    public void setDecisionPath(List<String> decisionPath) { this.decisionPath = decisionPath; }

    public List<String> getContributingFactors() { return contributingFactors; }
    public void setContributingFactors(List<String> contributingFactors) { this.contributingFactors = contributingFactors; }

    public List<String> getAcademicRecommendations() { return academicRecommendations; }
    public void setAcademicRecommendations(List<String> academicRecommendations) { this.academicRecommendations = academicRecommendations; }

    public boolean isAtRisk() { return isAtRisk; }
    public void setAtRisk(boolean atRisk) { isAtRisk = atRisk; }
}
