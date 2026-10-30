package com.edu.studentpredict.controller;

import com.edu.studentpredict.dto.DashboardStatsDto;
import com.edu.studentpredict.dto.StudentRequestDto;
import com.edu.studentpredict.dto.StudentResponseDto;
import com.edu.studentpredict.model.PerformanceLevel;
import com.edu.studentpredict.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Student Information & CRUD", description = "Endpoints for Experiments 2 & 7: Managing Student Records")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all students (supports filtering, sorting, pagination)")
    public ResponseEntity<Page<StudentResponseDto>> getAllStudents(
            @RequestParam(required = false) String course,
            @RequestParam(required = false) PerformanceLevel performance,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {

        String[] sortParts = sort.split(",");
        String sortField = sortParts[0];
        Sort.Direction direction = (sortParts.length > 1 && sortParts[1].equalsIgnoreCase("asc"))
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<StudentResponseDto> result = studentService.getAllStudents(course, performance, search, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    @Operation(summary = "Retrieve complete unpaginated list of students")
    public ResponseEntity<List<StudentResponseDto>> getAllStudentsUnpaginated() {
        return ResponseEntity.ok(studentService.getAllStudentsList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a single student record by numeric ID")
    public ResponseEntity<StudentResponseDto> getStudentById(@PathVariable Long id) {
        StudentResponseDto student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/lookup/{studentId}")
    @Operation(summary = "Retrieve a student record by student institutional ID")
    public ResponseEntity<StudentResponseDto> getStudentByStudentId(@PathVariable String studentId) {
        StudentResponseDto student = studentService.getStudentByStudentId(studentId);
        return ResponseEntity.ok(student);
    }

    @PostMapping
    @Operation(summary = "Create a new student record and trigger Decision Tree performance prediction")
    public ResponseEntity<StudentResponseDto> createStudent(@Valid @RequestBody StudentRequestDto requestDto) {
        StudentResponseDto created = studentService.createStudent(requestDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing student record and re-evaluate prediction")
    public ResponseEntity<StudentResponseDto> updateStudent(@PathVariable Long id,
                                                            @Valid @RequestBody StudentRequestDto requestDto) {
        StudentResponseDto updated = studentService.updateStudent(id, requestDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a student record by ID")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/at-risk")
    @Operation(summary = "Retrieve students identified as at-risk (attendance < 75% or Low performance)")
    public ResponseEntity<List<StudentResponseDto>> getAtRiskStudents() {
        return ResponseEntity.ok(studentService.getAtRiskStudents());
    }

    @GetMapping("/stats")
    @Operation(summary = "Retrieve aggregated dashboard metrics (counts, averages, course breakdown)")
    public ResponseEntity<DashboardStatsDto> getDashboardStats() {
        return ResponseEntity.ok(studentService.getDashboardStats());
    }
}
