package com.backend.senior_backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backend.senior_backend.models.Categories;
import com.backend.senior_backend.models.CategoriesId;

public interface CategoriesRepository extends JpaRepository<Categories, CategoriesId>{
    List<Categories> findAllByGroupId(Long groupId);
    List<Categories> findAllByGroupIdIn(List<Long> groupIds);
}
