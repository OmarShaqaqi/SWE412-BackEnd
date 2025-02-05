package com.backend.senior_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignupRequestDTO {

    @NotBlank(message = "Phone number is required")
    @Pattern(
        regexp = "^(\\+996\\d{9}|05\\d{8})$",
        message = "Invalid phone number format. Must be +996 followed by 9 digits OR start with 05 followed by 8 digits."
    )
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "First name is required")
    private String fname;

    @NotBlank(message = "Last name is required")
    private String lname;

    @NotBlank(message = "Username is required")
    private String username;
}

