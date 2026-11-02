package com.example.CourseRegistration.exception;


import com.example.CourseRegistration.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //common method to create error response body in json
    private ApiErrorResponse buildError(HttpStatus status,
                                        String message, String path ,
                                        Map<String,String> validateErrors){
        return ApiErrorResponse.builder()
                .timeStamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .validationErrors(validateErrors)
                .build();
    }

    //404 Response not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            ResourceNotFoundException ex, HttpServletRequest request
    ){
       ApiErrorResponse response = buildError(
               HttpStatus.NOT_FOUND,
               ex.getMessage(),
               request.getRequestURI(),
               null
       ) ;
       return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    //409 Duplicate Resource
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicate(
            DuplicateResourceException ex, HttpServletRequest request
    ){
        ApiErrorResponse response = buildError(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    //400 Validation Error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException ex ,
            HttpServletRequest request
    ){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error -> errors.putIfAbsent(
                        error.getField(),
                        error.getDefaultMessage()
                )
        );

        ApiErrorResponse response = buildError(
                HttpStatus.BAD_REQUEST,
                "Validation Error",
                request.getRequestURI(),
                errors
        );

        return ResponseEntity.badRequest().body(response);

    }
    //400 Constraint Violation
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request
    ){
        ApiErrorResponse response = buildError(
                HttpStatus.BAD_REQUEST,
                "Request Validation Failed",
                request.getRequestURI(),
                null
        );

        return ResponseEntity.badRequest().body(response);

    }
    //400 Invalid JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handelInvalidJson(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ){
        ApiErrorResponse response = buildError(
                HttpStatus.BAD_REQUEST,
                "Invalid JSON or Missing request body",
                request.getRequestURI(),
                null
        );

       return ResponseEntity.badRequest().body(response);
    }
    //409 Database Constraint Violation
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDatabaseConflict(
            DataIntegrityViolationException ex,
            HttpServletRequest request
    ){
        ApiErrorResponse response = buildError(
                HttpStatus.CONFLICT,
                "A record with value already exist or violates database constraint",
                request.getRequestURI(),
                null
        );

       return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    //500 Unexpected Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralException(
            Exception ex,
            HttpServletRequest request
    ){
        ApiErrorResponse response = buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again later",
                request.getRequestURI(),
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
