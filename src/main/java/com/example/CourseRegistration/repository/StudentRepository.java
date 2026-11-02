package com.example.CourseRegistration.repository;

import com.example.CourseRegistration.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    //Check weather an email already exists
    boolean existsByEmail(String email);
}
