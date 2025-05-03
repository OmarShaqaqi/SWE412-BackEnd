package com.backend.senior_backend.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Groups {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    @NotBlank(message = "Group name is required")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "Budget is required")
    private int budget;

    @Column(nullable = false)
    private String iconName;

}
