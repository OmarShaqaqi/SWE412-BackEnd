package com.backend.senior_backend.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "Complians")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Complian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date date;

    // make sure the message is not null and has a maximum length of 255 characters
    @Column(nullable = false, length = 255)
    private String message;

    @Column(name = "user_phone", nullable = false)
    private String userPhone;

    @Column(name = "finshed", nullable = false)
    private boolean status;

}
