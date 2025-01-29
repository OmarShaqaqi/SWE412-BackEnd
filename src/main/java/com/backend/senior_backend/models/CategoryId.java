package com.backend.senior_backend.models;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryId implements Serializable {
    private Long groupId;
    private String name;
}
