package com.edu.studentpredict.service;

import com.edu.studentpredict.dto.DashboardStatsDto;
import com.edu.studentpredict.dto.PredictionRequestDto;
import com.edu.studentpredict.dto.PredictionResponseDto;
import com.edu.studentpredict.dto.StudentRequestDto;
import com.edu.studentpredict.dto.StudentResponseDto;
import com.edu.studentpredict.exception.ResourceNotFoundException;
import com.edu.studentpredict.model.PerformanceLevel;
import com.edu.studentpredict.model.Student;
import com.edu.studentpredict.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final PredictionService predictionService;

    public StudentService(StudentRepository studentRepository, PredictionService predictionService) {
        this.studentRepository = studentRepository;
        this.predictionService = predictionService;
    }

    public Page<StudentResponseDto> getAllStudents(String course, PerformanceLevel performance, String search, Pageable pageable) {
        return studentRepository.findWithFilters(course, performance, search, pageable)
                .map(this::mapToResponseDto);
    }

    public List<StudentResponseDto> getAllStudentsList() {
        return studentRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public StudentResponseDto getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        return mapToResponseDto(student);
    }

    public StudentResponseDto getStudentByStudentId(String studentId) {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with studentId: " + studentId));
        return mapToResponseDto(student);
    }

    public StudentResponseDto createStudent(StudentRequestDto dto) {
        if (studentRepository.existsByStudentId(dto.getStudentId())) {
            throw new IllegalArgumentException("Student ID '" + dto.getStudentId() + "' already exists in the system.");
        }

        Student student = new Student();
        mapRequestToEntity(dto, student);

        // Run ML Prediction to classify performance automatically
        PredictionRequestDto predReq = createPredictionRequest(student);
        PredictionResponseDto predRes = predictionService.predictPerformance(predReq);

        student.setPredictedPerformance(predRes.getPredictedPerformance());
        student.setPredictionConfidence(predRes.getConfidence());
        student.setPredictionTimestamp(LocalDateTime.now());

        Student saved = studentRepository.save(student);
        return mapToResponseDto(saved);
    }

    public StudentResponseDto updateStudent(Long id, StudentRequestDto dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        // Check if studentId changed and clashes
        if (!student.getStudentId().equals(dto.getStudentId()) && studentRepository.existsByStudentId(dto.getStudentId())) {
            throw new IllegalArgumentException("Student ID '" + dto.getStudentId() + "' is already registered to another student.");
        }

        mapRequestToEntity(dto, student);

        // Re-evaluate ML prediction based on updated academic metrics
        PredictionRequestDto predReq = createPredictionRequest(student);
        PredictionResponseDto predRes = predictionService.predictPerformance(predReq);

        student.setPredictedPerformance(predRes.getPredictedPerformance());
        student.setPredictionConfidence(predRes.getConfidence());
        student.setPredictionTimestamp(LocalDateTime.now());

        Student updated = studentRepository.save(student);
        return mapToResponseDto(updated);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with ID: " + id);
        }
        studentRepository.deleteById(id);
    }

    public List<StudentResponseDto> getAtRiskStudents() {
        return studentRepository.findAtRiskStudents().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public DashboardStatsDto getDashboardStats() {
        DashboardStatsDto stats = new DashboardStatsDto();
        stats.setTotalStudents(studentRepository.count());
        stats.setGoodPerformanceCount(studentRepository.countByPredictedPerformance(PerformanceLevel.GOOD));
        stats.setAveragePerformanceCount(studentRepository.countByPredictedPerformance(PerformanceLevel.AVERAGE));
        stats.setLowPerformanceCount(studentRepository.countByPredictedPerformance(PerformanceLevel.LOW));
        stats.setAtRiskCount(studentRepository.countAtRiskStudents());

        Double avgAtt = studentRepository.getAverageAttendance();
        stats.setAverageAttendance(avgAtt != null ? Math.round(avgAtt * 10.0) / 10.0 : 0.0);

        Double avgMarks = studentRepository.getAveragePreviousMarks();
        stats.setAveragePreviousMarks(avgMarks != null ? Math.round(avgMarks * 10.0) / 10.0 : 0.0);

        Map<String, Long> byCourse = new HashMap<>();
        List<Object[]> courseCounts = studentRepository.countStudentsGroupedByCourse();
        for (Object[] row : courseCounts) {
            byCourse.put((String) row[0], (Long) row[1]);
        }
        stats.setStudentsByCourse(byCourse);

        return stats;
    }

    @PostConstruct
    public void seedInitialDataIfEmpty() {
        if (studentRepository.count() > 0) return;

        List<StudentRequestDto> seeds = Arrays.asList(
            createSeed("STU1001", "Aarav Sharma", "aarav.sharma@campus.edu", "Computer Science", 4, 94.0, 6.5, 88.0, 95.0, 28.0, 2),
            createSeed("STU1002", "Priya Patel", "priya.patel@campus.edu", "Information Technology", 4, 52.0, 2.0, 41.0, 45.0, 12.0, 18),
            createSeed("STU1003", "Rohan Verma", "rohan.verma@campus.edu", "Computer Science", 6, 78.0, 4.0, 68.0, 80.0, 21.0, 6),
            createSeed("STU1004", "Sneha Chauhan", "sneha.chauhan@campus.edu", "Data Science", 2, 96.0, 7.5, 93.0, 98.0, 29.0, 1),
            createSeed("STU1005", "Vikram Singh", "vikram.singh@campus.edu", "Computer Science", 4, 48.0, 1.5, 38.0, 40.0, 10.0, 22),
            createSeed("STU1006", "Ananya Gupta", "ananya.gupta@campus.edu", "Electronics Engineering", 4, 82.0, 4.5, 74.0, 85.0, 23.0, 5),
            createSeed("STU1007", "Aditya Joshi", "aditya.joshi@campus.edu", "Software Engineering", 5, 88.0, 5.5, 82.0, 90.0, 25.0, 3),
            createSeed("STU1008", "Kavya Iyer", "kavya.iyer@campus.edu", "Information Technology", 3, 62.0, 2.5, 52.0, 60.0, 15.0, 12),
            createSeed("STU1009", "Rahul Nair", "rahul.nair@campus.edu", "Data Science", 4, 91.0, 6.0, 86.0, 92.0, 27.0, 2),
            createSeed("STU1010", "Pooja Reddy", "pooja.reddy@campus.edu", "Software Engineering", 6, 56.0, 2.0, 44.0, 50.0, 13.0, 16)
        );

        for (StudentRequestDto seed : seeds) {
            createStudent(seed);
        }
    }

    private StudentRequestDto createSeed(String id, String name, String email, String course, int sem,
                                         double att, double hours, double prev, double assign, double internal, int missed) {
        StudentRequestDto s = new StudentRequestDto();
        s.setStudentId(id); s.setName(name); s.setEmail(email); s.setCourse(course); s.setSemester(sem);
        s.setAttendancePercentage(att); s.setStudyHoursPerDay(hours); s.setPreviousMarks(prev);
        s.setAssignmentCompletion(assign); s.setInternalMarks(internal); s.setMissedClasses(missed);
        return s;
    }

    private void mapRequestToEntity(StudentRequestDto dto, Student s) {
        s.setStudentId(dto.getStudentId());
        s.setName(dto.getName());
        s.setEmail(dto.getEmail());
        s.setCourse(dto.getCourse());
        s.setSemester(dto.getSemester());
        s.setAttendancePercentage(dto.getAttendancePercentage());
        s.setStudyHoursPerDay(dto.getStudyHoursPerDay());
        s.setPreviousMarks(dto.getPreviousMarks());
        s.setAssignmentCompletion(dto.getAssignmentCompletion());
        s.setInternalMarks(dto.getInternalMarks());
        s.setMissedClasses(dto.getMissedClasses());
    }

    private StudentResponseDto mapToResponseDto(Student s) {
        StudentResponseDto dto = new StudentResponseDto();
        dto.setId(s.getId());
        dto.setStudentId(s.getStudentId());
        dto.setName(s.getName());
        dto.setEmail(s.getEmail());
        dto.setCourse(s.getCourse());
        dto.setSemester(s.getSemester());
        dto.setAttendancePercentage(s.getAttendancePercentage());
        dto.setStudyHoursPerDay(s.getStudyHoursPerDay());
        dto.setPreviousMarks(s.getPreviousMarks());
        dto.setAssignmentCompletion(s.getAssignmentCompletion());
        dto.setInternalMarks(s.getInternalMarks());
        dto.setMissedClasses(s.getMissedClasses());
        dto.setPredictedPerformance(s.getPredictedPerformance());
        dto.setPredictionConfidence(s.getPredictionConfidence());
        dto.setPredictionTimestamp(s.getPredictionTimestamp());
        dto.setAtRisk(s.isAtRisk());
        return dto;
    }

    private PredictionRequestDto createPredictionRequest(Student s) {
        PredictionRequestDto req = new PredictionRequestDto();
        req.setStudentId(s.getStudentId());
        req.setName(s.getName());
        req.setCourse(s.getCourse());
        req.setSemester(s.getSemester());
        req.setAttendancePercentage(s.getAttendancePercentage());
        req.setStudyHoursPerDay(s.getStudyHoursPerDay());
        req.setPreviousMarks(s.getPreviousMarks());
        req.setAssignmentCompletion(s.getAssignmentCompletion());
        req.setInternalMarks(s.getInternalMarks());
        req.setMissedClasses(s.getMissedClasses());
        return req;
    }
}
