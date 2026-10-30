package com.edu.studentpredict.dto;

import java.util.Map;

public class DashboardStatsDto {
    private long totalStudents;
    private long goodPerformanceCount;
    private long averagePerformanceCount;
    private long lowPerformanceCount;
    private long atRiskCount;
    private double averageAttendance;
    private double averagePreviousMarks;
    private Map<String, Long> studentsByCourse;

    public DashboardStatsDto() {}

    // Getters and Setters
    public long getTotalStudents() { return totalStudents; }
    public void setTotalStudents(long totalStudents) { this.totalStudents = totalStudents; }

    public long getGoodPerformanceCount() { return goodPerformanceCount; }
    public void setGoodPerformanceCount(long goodPerformanceCount) { this.goodPerformanceCount = goodPerformanceCount; }

    public long getAveragePerformanceCount() { return averagePerformanceCount; }
    public void setAveragePerformanceCount(long averagePerformanceCount) { this.averagePerformanceCount = averagePerformanceCount; }

    public long getLowPerformanceCount() { return lowPerformanceCount; }
    public void setLowPerformanceCount(long lowPerformanceCount) { this.lowPerformanceCount = lowPerformanceCount; }

    public long getAtRiskCount() { return atRiskCount; }
    public void setAtRiskCount(long atRiskCount) { this.atRiskCount = atRiskCount; }

    public double getAverageAttendance() { return averageAttendance; }
    public void setAverageAttendance(double averageAttendance) { this.averageAttendance = averageAttendance; }

    public double getAveragePreviousMarks() { return averagePreviousMarks; }
    public void setAveragePreviousMarks(double averagePreviousMarks) { this.averagePreviousMarks = averagePreviousMarks; }

    public Map<String, Long> getStudentsByCourse() { return studentsByCourse; }
    public void setStudentsByCourse(Map<String, Long> studentsByCourse) { this.studentsByCourse = studentsByCourse; }
}
