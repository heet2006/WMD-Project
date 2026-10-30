package com.edu.studentpredict.dto;

import jakarta.validation.constraints.*;

public class StudentRequestDto {

    @NotBlank(message = "Student ID is required (e.g. STU1001)")
    @Size(min = 3, max = 30, message = "Student ID must be between 3 and 30 characters")
    private String studentId;

    @NotBlank(message = "Student name is required")
    @Size(min = 2, max = 100, message = "Student name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Valid email address format is required")
    private String email;

    @NotBlank(message = "Course is required")
    private String course;

    @NotNull(message = "Semester is required")
    @Min(value = 1, message = "Semester must be between 1 and 8")
    @Max(value = 8, message = "Semester must be between 1 and 8")
    private Integer semester;

    @NotNull(message = "Attendance percentage is required")
    @DecimalMin(value = "0.0", message = "Attendance must be >= 0.0%")
    @DecimalMax(value = "100.0", message = "Attendance must be <= 100.0%")
    private Double attendancePercentage;

    @NotNull(message = "Study hours per day is required")
    @DecimalMin(value = "0.0", message = "Study hours must be >= 0.0")
    @DecimalMax(value = "24.0", message = "Study hours must be <= 24.0")
    private Double studyHoursPerDay;

    @NotNull(message = "Previous marks percentage is required")
    @DecimalMin(value = "0.0", message = "Previous marks must be >= 0.0%")
    @DecimalMax(value = "100.0", message = "Previous marks must be <= 100.0%")
    private Double previousMarks;

    @NotNull(message = "Assignment completion is required")
    @DecimalMin(value = "0.0", message = "Assignment completion must be >= 0.0%")
    @DecimalMax(value = "100.0", message = "Assignment completion must be <= 100.0%")
    private Double assignmentCompletion;

    @NotNull(message = "Internal assessment marks are required")
    @DecimalMin(value = "0.0", message = "Internal marks must be >= 0.0")
    @DecimalMax(value = "30.0", message = "Internal marks must be <= 30.0")
    private Double internalMarks;

    @NotNull(message = "Missed classes count is required")
    @Min(value = 0, message = "Missed classes must be >= 0")
    private Integer missedClasses;

    public StudentRequestDto() {
    }

    // Getters and Setters
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
}
