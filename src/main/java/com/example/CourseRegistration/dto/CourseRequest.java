package com.example.CourseRegistration.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CourseRequest {

    @NotBlank(message = "Course name is required")
    @Size(min = 2, max = 100,
    message = "Course name must be between 2 to 100 characters")
    private String courseName;

    @NotBlank(message = "Course duration is required")
    private String duration;

    @NotNull(message = "Course fees is required" )
    @DecimalMin(value = "0.0",
    inclusive = false,
    message = "Fees must be greater then zero")
    @Digits(integer = 8, fraction = 2,
    message = "Fees can have 8 integer value and 2 decimal value")
    private BigDecimal fees;
}
