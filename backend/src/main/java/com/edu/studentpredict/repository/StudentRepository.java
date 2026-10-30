package com.edu.studentpredict.repository;

import com.edu.studentpredict.model.PerformanceLevel;
import com.edu.studentpredict.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByStudentId(String studentId);

    boolean existsByStudentId(String studentId);

    List<Student> findByCourse(String course);

    List<Student> findByPredictedPerformance(PerformanceLevel performanceLevel);

    long countByPredictedPerformance(PerformanceLevel performanceLevel);

    @Query("SELECT s FROM Student s WHERE s.attendancePercentage < 75.0 OR s.previousMarks < 50.0 OR s.predictedPerformance = 'LOW'")
    List<Student> findAtRiskStudents();

    @Query("SELECT COUNT(s) FROM Student s WHERE s.attendancePercentage < 75.0 OR s.previousMarks < 50.0 OR s.predictedPerformance = 'LOW'")
    long countAtRiskStudents();

    @Query("SELECT s FROM Student s WHERE " +
           "(:course IS NULL OR s.course = :course) AND " +
           "(:performance IS NULL OR s.predictedPerformance = :performance) AND " +
           "(:search IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(s.studentId) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Student> findWithFilters(@Param("course") String course,
                                  @Param("performance") PerformanceLevel performance,
                                  @Param("search") String search,
                                  Pageable pageable);

    @Query("SELECT AVG(s.attendancePercentage) FROM Student s")
    Double getAverageAttendance();

    @Query("SELECT AVG(s.previousMarks) FROM Student s")
    Double getAveragePreviousMarks();

    @Query("SELECT s.course, COUNT(s) FROM Student s GROUP BY s.course")
    List<Object[]> countStudentsGroupedByCourse();
}
