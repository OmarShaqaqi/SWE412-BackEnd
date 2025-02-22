package com.backend.senior_backend.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.backend.senior_backend.repositories.ExpenseRepository;
import com.backend.senior_backend.models.Users;
import com.backend.senior_backend.repositories.UsersRepository;
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
}