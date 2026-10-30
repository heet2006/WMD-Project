package com.edu.studentpredict.dto;

import com.edu.studentpredict.model.PerformanceLevel;
import java.time.LocalDateTime;

public class StudentResponseDto {
    private Long id;
    private String studentId;
    private String name;
    private String email;
    private String course;
    private Integer semester;
    private Double attendancePercentage;
    private Double studyHoursPerDay;
    private Double previousMarks;
    private Double assignmentCompletion;
    private Double internalMarks;
    private Integer missedClasses;
    private PerformanceLevel predictedPerformance;
    private Double predictionConfidence;
    private LocalDateTime predictionTimestamp;
    private boolean atRisk;

    public StudentResponseDto() {
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

    public boolean isAtRisk() { return atRisk; }
    public void setAtRisk(boolean atRisk) { this.atRisk = atRisk; }
}
