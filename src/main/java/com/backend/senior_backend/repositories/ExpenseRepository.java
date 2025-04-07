package com.backend.senior_backend.repositories;

import java.util.List;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import com.backend.senior_backend.models.CategoriesId;
import com.backend.senior_backend.models.Expenses;

public interface ExpenseRepository extends JpaRepository<Expenses, Long>{
    List<Expenses> findAllByCategoryId(CategoriesId categoryId);
    List<Expenses> findAllByCategoryIdGroupId(Long groupId);
    List<Expenses> findAllByCategoryIdGroupIdAndDate(Long groupId, Date date);
    List<Expenses> findByCategory_GroupIdAndUser_Phone(Long groupId, String phone);
    List<Expenses> findAllByUser_Phone(String phone);

}
