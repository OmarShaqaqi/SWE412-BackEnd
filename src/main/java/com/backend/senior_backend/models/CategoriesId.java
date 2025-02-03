package com.backend.senior_backend.models;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CategoriesId implements Serializable {
    private Long groupId;
    private String name;
}
