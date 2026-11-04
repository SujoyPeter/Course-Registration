package com.example.CourseRegistration.controller;

import com.example.CourseRegistration.dto.CourseRequest;
import com.example.CourseRegistration.dto.CourseResponse;
import com.example.CourseRegistration.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseResponse> addCourse(
            @Valid @RequestBody CourseRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(courseService.addCourse(request));
    }

    @GetMapping
    public List<CourseResponse> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public CourseResponse getCourseById(
            @PathVariable Long id) {

        return courseService.getCourseById(id);
    }
}
