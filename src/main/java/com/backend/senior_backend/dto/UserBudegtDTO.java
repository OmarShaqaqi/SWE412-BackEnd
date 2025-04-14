package com.backend.senior_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserBudegtDTO {
    private String phone;
    private String fname;
    private String lname;
    private String email;
    private String username;
    private String password;
    private Double budget;
}



