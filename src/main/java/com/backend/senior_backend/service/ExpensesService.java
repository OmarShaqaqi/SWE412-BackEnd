package com.backend.senior_backend.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.backend.senior_backend.repositories.ExpenseRepository;
import com.backend.senior_backend.models.Users;
import com.backend.senior_backend.repositories.UsersRepository;
import com.backend.senior_backend.dto.ExpensesResponse;
import com.backend.senior_backend.models.Categories;
import com.backend.senior_backend.models.CategoriesId;
import com.backend.senior_backend.models.Expenses;
import com.backend.senior_backend.repositories.CategoriesRepository;

@Service
public class ExpensesService {

    @Autowired
    private ExpenseRepository expensesRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    public String addExpense(Long groupId, String categoryName, String userPhone, BigDecimal amount, Date date, String description) {
        Optional<Users> userOpt = usersRepository.findByPhone(userPhone);
        if (userOpt.isEmpty()) {
            return "❌ User not found!";
        }

        CategoriesId categoriesId = new CategoriesId(groupId, categoryName);
        Optional<Categories> categoryOpt = categoriesRepository.findById(categoriesId);
        if (categoryOpt.isEmpty()) {
            return "❌ Category not found!";
        }

        Expenses expense = new Expenses();
        expense.setUser(userOpt.get());
        expense.setCategory(categoryOpt.get());
        expense.setAmount(amount);
        expense.setDate(date);
        expense.setDescription(description);
        expense.setStatus("pending");

        expensesRepository.save(expense);
        return "✅ Expense added successfully!";
    }

    public List<Expenses> getExpenses(Long groupId, String categoryName) {
        return expensesRepository.findAllByCategoryId(new CategoriesId(groupId, categoryName));
    }

    public String deleteExpense(Long expenseId) {
        Optional<Expenses> expenseOpt = expensesRepository.findById(expenseId);
        if (expenseOpt.isEmpty()) {
            return "❌ Expense not found!";
        }
        expensesRepository.delete(expenseOpt.get());
        return "✅ Expense deleted successfully!";
    }

    public List<ExpensesResponse> getExpensesByGroupId(Long groupId) {
        List<Expenses> expenses = expensesRepository.findAllByCategoryIdGroupId(groupId);
        // Map the Expenses entities to the ExpensesResponse DTO
        List<ExpensesResponse> response = expenses.stream()
            .map(e -> new ExpensesResponse(
                e.getId(),
                e.getDate(),
                e.getAmount(),
                e.getStatus(),
                e.getCategory().getId().getName()
            ))
            .collect(Collectors.toList());

        return response;
    }

    public boolean updateExpenseStatus(Long expenseId, String status, String leaderPhone) {
        Optional<Expenses> expenseOpt = expensesRepository.findById(expenseId);
        if (expenseOpt.isPresent()) {
            Expenses expense = expenseOpt.get();
            // Validate that the status is valid
            if (!status.equals("APPROVED") && !status.equals("REJECTED")) {
                return false; // Invalid status
            }
            expense.setStatus(status);
            expensesRepository.save(expense);
            return true;
        }
        return false;
    }
}