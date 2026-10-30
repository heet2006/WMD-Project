package com.edu.studentpredict.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "students", indexes = {
    @Index(name = "idx_student_id", columnList = "student_id", unique = true),
    @Index(name = "idx_course", columnList = "course"),
    @Index(name = "idx_performance", columnList = "predicted_performance")
})
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false, unique = true, length = 30)
    private String studentId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(nullable = false, length = 100)
    private String course;

    @Column(nullable = false)
    private Integer semester;

    @Column(name = "attendance_percentage", nullable = false)
    private Double attendancePercentage;

    @Column(name = "study_hours_per_day", nullable = false)
    private Double studyHoursPerDay;

    @Column(name = "previous_marks", nullable = false)
    private Double previousMarks;

    @Column(name = "assignment_completion", nullable = false)
    private Double assignmentCompletion;

    @Column(name = "internal_marks", nullable = false)
    private Double internalMarks;

    @Column(name = "missed_classes", nullable = false)
    private Integer missedClasses;

    @Enumerated(EnumType.STRING)
    @Column(name = "predicted_performance", length = 20)
    private PerformanceLevel predictedPerformance;

    @Column(name = "prediction_confidence")
    private Double predictionConfidence;

    @Column(name = "prediction_timestamp")
    private LocalDateTime predictionTimestamp;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Student() {
    }

    public Student(String studentId, String name, String email, String course, Integer semester,
                   Double attendancePercentage, Double studyHoursPerDay, Double previousMarks,
                   Double assignmentCompletion, Double internalMarks, Integer missedClasses) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.course = course;
        this.semester = semester;
        this.attendancePercentage = attendancePercentage;
        this.studyHoursPerDay = studyHoursPerDay;
        this.previousMarks = previousMarks;
        this.assignmentCompletion = assignmentCompletion;
        this.internalMarks = internalMarks;
        this.missedClasses = missedClasses;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.predictionTimestamp == null) {
            this.predictionTimestamp = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isAtRisk() {
        return (attendancePercentage != null && attendancePercentage < 75.0)
            || (previousMarks != null && previousMarks < 50.0)
            || PerformanceLevel.LOW.equals(predictedPerformance);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }

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

    public Double getPredictionConfidence() { return predictionConfidence; }
    public void setPredictionConfidence(Double predictionConfidence) { this.predictionConfidence = predictionConfidence; }

    public LocalDateTime getPredictionTimestamp() { return predictionTimestamp; }
    public void setPredictionTimestamp(LocalDateTime predictionTimestamp) { this.predictionTimestamp = predictionTimestamp; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
