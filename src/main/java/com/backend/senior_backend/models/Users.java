package com.backend.senior_backend.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@Data
public class Users {
    
    @Id
    @Column(length = 15, nullable = false, unique = true)
    private String phone;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 255, nullable = false)  // Increase password field to handle hash
    private String password;

    @Column(length = 50, nullable = false, unique = true)
    private String username;

    @Column(length = 30, nullable = false)
    private String fname;

    @Column(length = 30, nullable = false)
    private String lname;

    public Users(String phone, String fname, String lname, String email, String username, String password) {
        this.phone = phone;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
