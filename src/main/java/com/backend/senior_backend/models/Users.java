package com.backend.senior_backend.models;

import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

// @Entity
// @Table(name = "users")
// @Getter
// @Setter
// @NoArgsConstructor
// public class Users {
    
//     @Id
//     @Column(length = 15, nullable = false, unique = true)
//     private String phone;

//     @Column(length = 50, nullable = false, unique = true)
//     @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
//     + "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
//     + "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9]"
//     + "(?:[a-z0-9-]*[a-z0-9])?",
//     message = "{invalid.email}")
//     private String email;

//     @Column(length = 100, nullable = false)
//     @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
//     message = "{invalid.password}")
//     private String password;

//     @Column(length = 50, nullable = false,unique = true)
//     @Pattern(regexp = "^[_a-zA-Z]{3,16}$",
//     message = "{invalid.username}")
//     private String username;


//     @Column(length = 15, nullable = false)
//     private String fname;

//     @Column(length = 15, nullable = false)
//     private String lname;


//     private String resetPin; // 6-digit PIN for password reset
//     private LocalDateTime pinExpiration; // Expiration time for the PIN


//     public Users(String phone, String fname, String lname, String email, String password,String username) {
//         this.phone = phone;
//         this.fname = fname;
//         this.lname = lname;
//         this.email = email;
//         this.password = password;
//         this.username = username;
//     }
// }

@Entity
@Table(name = "users")  // Ensure lowercase table name
@Getter
@Setter
@NoArgsConstructor
public class Users {
    
    @Id
    @Column(length = 15, nullable = false, unique = true)
    private String phone;

    @Column(length = 100, nullable = false, unique = true)  // Allow longer emails
    private String email;
    //     @Column(length = 50, nullable = false, unique = true)
//     @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
//     + "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
//     + "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9]"
//     + "(?:[a-z0-9-]*[a-z0-9])?",
//     message = "{invalid.email}")
//     private String email;

    @Column(length = 255, nullable = false)  // Increase password field to handle hash
    private String password;

    @Column(length = 50, nullable = false, unique = true)
    private String username;

    @Column(length = 30, nullable = false)
    private String fname;

    @Column(length = 30, nullable = false)
    private String lname;

    private String resetPin;
    private LocalDateTime pinExpiration;

    public Users(String phone, String fname, String lname, String email, String username, String password) {
        this.phone = phone;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
