package com.backend.senior_backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Users implements UserDetails {
    
    @Id
    @Column(length = 15, nullable = false, unique = true)
    @NotBlank(message = "{invalid.phone}") 
    private String phone;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 255, nullable = false)  // Increase password field to handle hash, we need to make strong pass later
    private String password;

    @Column(length = 50, nullable = false, unique = true)
    private String username;

    @Column(length = 30, nullable = false)
    private String fname;

    @Column(length = 30, nullable = false)
    private String lname;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

}
