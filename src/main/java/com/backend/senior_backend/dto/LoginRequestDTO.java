package com.backend.senior_backend.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String phone;
    private String password;
}
