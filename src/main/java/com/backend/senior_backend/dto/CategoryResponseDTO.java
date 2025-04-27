
package com.backend.senior_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryResponseDTO {
    private Long groupId;

    private String categoryName;

    // Default constructor
    public CategoryResponseDTO() {}

    // Constructor with parameters
    public CategoryResponseDTO(Long groupId, String categoryName) {
        this.groupId = groupId;
        this.categoryName = categoryName;
    }

   
}
