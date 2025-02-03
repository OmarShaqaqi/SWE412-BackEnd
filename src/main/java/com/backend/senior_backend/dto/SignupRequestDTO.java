package com.backend.senior_backend.dto;

import lombok.Data;

@Data
public class SignupRequestDTO {
    private String phone;
    private String fname;
    private String lname;
    private String email;
    private String password;
    private String username;
}
