package com.backend.senior_backend.models;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Categories {

    @EmbeddedId
    private CategoriesId id;

    @ManyToOne
    @MapsId("groupId")  
    @JoinColumn(name = "group_id", nullable = false)
    private Groups group;

    @Column(nullable = false)
    private String iconName;

    
}
