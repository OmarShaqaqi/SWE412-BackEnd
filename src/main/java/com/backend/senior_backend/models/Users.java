package com.backend.senior_backend.models;

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

    @Column(length = 15, nullable = false)
    private String fname;

    @Column(length = 15, nullable = false)
    private String lname;
}
