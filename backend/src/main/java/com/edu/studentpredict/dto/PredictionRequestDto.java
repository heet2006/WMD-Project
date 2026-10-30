package com.edu.studentpredict.dto;

import jakarta.validation.constraints.*;
import java.util.List;
import java.util.Map;

public class PredictionRequestDto {

    private String studentId;
    private String name;
    private String course;
    private Integer semester;

    @NotNull(message = "Attendance percentage is required")
    @DecimalMin("0.0") @DecimalMax("100.0")
    private Double attendancePercentage;

    @NotNull(message = "Study hours per day is required")
    @DecimalMin("0.0") @DecimalMax("24.0")
    private Double studyHoursPerDay;

    @NotNull(message = "Previous marks percentage is required")
    @DecimalMin("0.0") @DecimalMax("100.0")
    private Double previousMarks;

    @NotNull(message = "Assignment completion is required")
    @DecimalMin("0.0") @DecimalMax("100.0")
    private Double assignmentCompletion;

    @NotNull(message = "Internal assessment marks are required")
    @DecimalMin("0.0") @DecimalMax("30.0")
    private Double internalMarks;

    @NotNull(message = "Missed classes count is required")
    @Min(0)
    private Integer missedClasses;

    public PredictionRequestDto() {}

    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

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
}
