package com.example.CourseRegistration.controller;

import com.example.CourseRegistration.dto.StudentRequest;
import com.example.CourseRegistration.dto.StudentResponse;
import com.example.CourseRegistration.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentResponse> addStudent(
            @Valid @RequestBody StudentRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentService.addStudent(request));
    }

    @GetMapping
    public List<StudentResponse> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public StudentResponse getStudentById(
            @PathVariable Long id) {

        return studentService.getStudentById(id);
    }

    @PutMapping("/{id}")
    public StudentResponse updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequest request) {

        return studentService.updateStudent(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable Long id) {

        studentService.deleteStudent(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{studentId}/courses/{courseId}")
    public StudentResponse registerCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {

        return studentService.registerCourse(studentId, courseId);
    }

    @DeleteMapping("/{studentId}/courses/{courseId}")
    public StudentResponse removeCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {

        return studentService.removeCourse(studentId, courseId);
    }
}
