package com.backend.senior_backend.models;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Category {

    @EmbeddedId
    private CategoryId id;

    @ManyToOne
    @MapsId("groupId")  // Reference to group_id in CategoryId
    @JoinColumn(name = "group_id", nullable = false)
    private Groups group;

    // Manually add getters and setters for ID
    // @Data adds getters and setters for all fields
    public CategoryId getId() {
        return id;
    }

    public void setId(CategoryId id) {
        this.id = id;
    }
}
