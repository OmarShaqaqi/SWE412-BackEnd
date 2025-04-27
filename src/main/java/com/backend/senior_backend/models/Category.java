package com.backend.senior_backend.models;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "Category")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Category {

    @EmbeddedId
    private CategoriesId id;

    @ManyToOne
    @MapsId("groupId")  // Reference to group_id in CategoryId
    @JoinColumn(name = "group_id", nullable = false)
    private Groups group;

}
