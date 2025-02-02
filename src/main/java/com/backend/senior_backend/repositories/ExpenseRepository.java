package com.backend.senior_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.senior_backend.models.Expenses;

public interface ExpenseRepository extends JpaRepository<Expenses, Long>{

}
