package com.backend.senior_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backend.senior_backend.models.Categories;
import com.backend.senior_backend.models.CategoriesId;

public interface CategoriesRepository extends JpaRepository<Categories, CategoriesId>{

}
