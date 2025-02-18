package com.backend.senior_backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.senior_backend.models.Expenses;

public interface ExpenseRepository extends JpaRepository<Expenses, Long>{
    List<Expenses> findByUser_PhoneAndCategory_Group_Id(String userPhone, Long groupId);
}
