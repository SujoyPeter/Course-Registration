package com.example.CourseRegistration.service;

import com.example.CourseRegistration.dto.CourseResponse;
import com.example.CourseRegistration.dto.StudentRequest;
import com.example.CourseRegistration.dto.StudentResponse;
import com.example.CourseRegistration.entity.Course;
import com.example.CourseRegistration.entity.Student;
import com.example.CourseRegistration.exception.DuplicateResourceException;
import com.example.CourseRegistration.exception.ResourceNotFoundException;
import com.example.CourseRegistration.repository.CourseRepository;
import com.example.CourseRegistration.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentService(
            StudentRepository studentRepository,
            CourseRepository courseRepository
    ){
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    private StudentResponse convertToResponse(Student student){
        List<CourseResponse> courses = student.getCourses().stream().
                map(course -> new CourseResponse(
                        course.getId(),
                        course.getCourseName(),
                        course.getDuration(),
                        course.getFees()
                )).toList();

        return new StudentResponse(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getMobile(),
                courses
        );
    }

    private Student findStudent(Long id){
        return studentRepository.findById(id).
                orElseThrow(() ->
        new ResourceNotFoundException(
                "Student not found with ID: " + id ));
    }

    //CRUD operations
    //add student
    @Transactional
    public StudentResponse addStudent(StudentRequest request){
        String email = request.getEmail().trim().toLowerCase();
        if(studentRepository.existsByEmail(email)){
            throw new DuplicateResourceException(
                    "Student email already exists: " + email );
        }
        Student student = new Student();
        student.setName(request.getName().trim());
        student.setEmail(email);
        student.setMobile(request.getMobile());

        Student savedStudent = studentRepository.save(student);
        return convertToResponse(savedStudent);
    }

    //Get all the students
    @Transactional(readOnly = true)
    public List<StudentResponse> getAllStudents(){
        return studentRepository.findAll().stream().
                map(this :: convertToResponse).toList();
    }

    //Get student by Id
    @Transactional(readOnly = true)
    public StudentResponse getStudentById(Long id){
        return convertToResponse(findStudent(id));
    }

    //Update Student
    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest request){
       Student student = findStudent(id);
       String email = request.getEmail().trim().toLowerCase();
       if(!student.getEmail().equals(email)
       && studentRepository.existsByEmail(email)){
          throw new DuplicateResourceException(
                  "Email is already used by another student: " + email);
       }
       student.setName(request.getName().trim());
       student.setEmail(email);
       student.setMobile(request.getMobile());

       return convertToResponse(studentRepository.save(student));
    }

    //Delete Student
    @Transactional
    public void deleteStudent(Long id){
        Student student = findStudent(id);
        student.getCourses().clear();
        studentRepository.delete(student);
    }

    //registering student for course
    @Transactional
    public StudentResponse registerCourse(Long studentId, Long courseId){
        Student student = findStudent(studentId);
        Course course = courseRepository.findById(courseId).
                orElseThrow(() ->
                        new ResourceNotFoundException(
                          "Course not found with id: " + courseId
                        ));


        if(!student.getCourses().add(course)){
            throw new DuplicateResourceException(
                    "Student is already registered for this course"
            );
        }
        return convertToResponse(student);
    }

    //remove course
    @Transactional
    public StudentResponse removeCourse(Long studentId, Long courseId){
        Student student = findStudent(studentId);
        Course course = courseRepository.findById(courseId).
                orElseThrow(() ->
                        new ResourceNotFoundException(
                             "Course not found by Id: " + courseId
                        ));

        if (!student.getCourses().remove(course)){
            throw new ResourceNotFoundException(
                    "Student is not registered for this course"
            );
        }
        return convertToResponse(student);
    }
}
