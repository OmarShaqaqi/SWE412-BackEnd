package com.backend.senior_backend.models;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Groups {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(nullable = false)
    private int budget;

    @ManyToOne
    @JoinColumn(name = "leader_phone", nullable = false)
    private Users leader;
}
