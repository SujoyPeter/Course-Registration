package com.example.CourseRegistration.exception;


import com.example.CourseRegistration.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
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
    //400 Validation Error
    //400 Constraint Violation
    //400 Invalid JSON
    //409 Database Constraint Violation
    //500 Unexpected Error
}
