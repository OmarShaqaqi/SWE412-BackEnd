package com.backend.senior_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDTO {
    private String phone;
    private String fname;
    private String lname;
    private String email;
    private String username;
}
