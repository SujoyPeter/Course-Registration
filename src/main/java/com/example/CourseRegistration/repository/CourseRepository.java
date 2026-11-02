package com.example.CourseRegistration.repository;

import com.example.CourseRegistration.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    //Check whether a course already exists
    boolean existsByCourseNameIgnoreCase(String courseName);
}
