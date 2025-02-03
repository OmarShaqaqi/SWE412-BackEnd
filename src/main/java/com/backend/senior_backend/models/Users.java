package com.backend.senior_backend.models;

import jakarta.validation.constraints.Pattern;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    
    @Id
    @Column(length = 15, nullable = false, unique = true)
    private String phone;

    @Column(length = 50, nullable = false, unique = true)
    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
    + "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
    + "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9]"
    + "(?:[a-z0-9-]*[a-z0-9])?",
    message = "{invalid.email}")
    private String email;

    @Column(length = 50, nullable = false)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
    message = "{invalid.password}")
    private String password;

    @Column(length = 50, nullable = false,unique = true)
    @Pattern(regexp = "^[_a-zA-Z]{3,16}$",
    message = "{invalid.username}")
    private String username;


    @Column(length = 15, nullable = false)
    private String fname;

    @Column(length = 15, nullable = false)
    private String lname;
}
