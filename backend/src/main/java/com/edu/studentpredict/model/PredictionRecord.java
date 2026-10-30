package com.edu.studentpredict.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prediction_records")
public class PredictionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", length = 30)
    private String studentId;

    @Column(length = 100)
    private String studentName;

    @Column(name = "attendance_percentage")
    private Double attendancePercentage;

    @Column(name = "study_hours_per_day")
    private Double studyHoursPerDay;

    @Column(name = "previous_marks")
    private Double previousMarks;

    @Column(name = "assignment_completion")
    private Double assignmentCompletion;

    @Column(name = "internal_marks")
    private Double internalMarks;

    @Column(name = "missed_classes")
    private Integer missedClasses;

    @Enumerated(EnumType.STRING)
    @Column(name = "predicted_performance", length = 20)
    private PerformanceLevel predictedPerformance;

    @Column(name = "confidence")
    private Double confidence;

    @Column(name = "contributing_factors", columnDefinition = "TEXT")
    private String contributingFactors;

    @Column(name = "recommendations", columnDefinition = "TEXT")
    private String recommendations;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    public PredictionRecord() {
    }

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public Double getAttendancePercentage() { return attendancePercentage; }
    public void setAttendancePercentage(Double attendancePercentage) { this.attendancePercentage = attendancePercentage; }

    public Double getStudyHoursPerDay() { return studyHoursPerDay; }
    public void setStudyHoursPerDay(Double studyHoursPerDay) { this.studyHoursPerDay = studyHoursPerDay; }

    public Double getPreviousMarks() { return previousMarks; }
    public void setPreviousMarks(Double previousMarks) { this.previousMarks = previousMarks; }

    public Double getAssignmentCompletion() { return assignmentCompletion; }
    public void setAssignmentCompletion(Double assignmentCompletion) { this.assignmentCompletion = assignmentCompletion; }

    public Double getInternalMarks() { return internalMarks; }
    public void setInternalMarks(Double internalMarks) { this.internalMarks = internalMarks; }

    public Integer getMissedClasses() { return missedClasses; }
    public void setMissedClasses(Integer missedClasses) { this.missedClasses = missedClasses; }

    public PerformanceLevel getPredictedPerformance() { return predictedPerformance; }
    public void setPredictedPerformance(PerformanceLevel predictedPerformance) { this.predictedPerformance = predictedPerformance; }

    public Double getConfidence() { return confidence; }
    public void setConfidence(Double confidence) { this.confidence = confidence; }

    public String getContributingFactors() { return contributingFactors; }
    public void setContributingFactors(String contributingFactors) { this.contributingFactors = contributingFactors; }

    public String getRecommendations() { return recommendations; }
    public void setRecommendations(String recommendations) { this.recommendations = recommendations; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
