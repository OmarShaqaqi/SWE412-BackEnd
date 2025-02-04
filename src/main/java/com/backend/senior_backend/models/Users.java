package com.backend.senior_backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

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
    @NotBlank(message = "{invalid.phone}") 
    private String phone;

    @Column(length = 50, nullable = false, unique = true)
    @NotBlank(message = "{invalid.email}")
    @Email(message = "{invalid.email}")
    private String email;

    @Column(length = 50, nullable = false)
    @NotBlank(message = "{invalid.password}")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "{invalid.password}"
    )
    private String password;

    @Column(length = 50, nullable = false, unique = true)
    @NotBlank(message = "{invalid.username}")
    @Pattern(
        regexp = "^[_a-zA-Z]{3,16}$",
        message = "{invalid.username}"
    )
    private String username;

    @Column(length = 15, nullable = false)
    @NotBlank(message = "{invalid.fname}")
    private String fname;

    @Column(length = 15, nullable = false)
    @NotBlank(message = "{invalid.lname}")
    private String lname;
}
