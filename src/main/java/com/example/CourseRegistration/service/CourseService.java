package com.example.CourseRegistration.service;

import com.example.CourseRegistration.dto.CourseRequest;
import com.example.CourseRegistration.dto.CourseResponse;
import com.example.CourseRegistration.entity.Course;
import com.example.CourseRegistration.exception.DuplicateResourceException;
import com.example.CourseRegistration.exception.ResourceNotFoundException;
import com.example.CourseRegistration.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    private CourseResponse convertToResponse(Course course) {

        return new CourseResponse(
                course.getId(),
                course.getCourseName(),
                course.getDuration(),
                course.getFees()
        );
    }

    @Transactional
    public CourseResponse addCourse(CourseRequest request) {

        String courseName = request.getCourseName().trim();

        if (courseRepository.existsByCourseNameIgnoreCase(courseName)) {
            throw new DuplicateResourceException(
                    "Course already exists: " + courseName
            );
        }

        Course course = new Course();

        course.setCourseName(courseName);
        course.setDuration(request.getDuration().trim());
        course.setFees(request.getFees());

        return convertToResponse(courseRepository.save(course));
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourses() {

        return courseRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found with ID: " + id
                        ));

        return convertToResponse(course);
    }
}
