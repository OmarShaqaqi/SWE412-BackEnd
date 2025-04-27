package com.backend.senior_backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.backend.senior_backend.repositories.ExpenseRepository;
import com.backend.senior_backend.repositories.ParticipantsRepository;
import com.backend.senior_backend.models.Users;
import com.backend.senior_backend.repositories.UsersRepository;
import com.backend.senior_backend.dto.ExpensesDetails;
import com.backend.senior_backend.dto.ExpensesResponse;
import com.backend.senior_backend.dto.ExpensesSummaryDTO;
import com.backend.senior_backend.models.Categories;
import com.backend.senior_backend.models.CategoriesId;
import com.backend.senior_backend.models.Expenses;
import com.backend.senior_backend.models.Groups;
import com.backend.senior_backend.repositories.CategoriesRepository;

@Service
public class ExpensesService {

    public enum TimeGroup {
        DAY, MONTH, YEAR
    }

    @Autowired
    private ExpenseRepository expensesRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private ParticipantsRepository participantsRepository;

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

    public double getTotalExpenses(Long groupId, String phone) {

        List<Expenses> expensesList = expensesRepository.findByCategory_GroupIdAndUser_Phone(groupId, phone);
        
        // Calculate the total amount
        BigDecimal totalAmount = expensesList.stream()
            .map(Expenses::getAmount) // Extract the amount from each expense
            .reduce(BigDecimal.ZERO, BigDecimal::add); // Sum up the amounts

        // Return the total amount as a double
        return totalAmount.doubleValue();
    }
    public double getTotalExpensesAllUsers(Long groupId) {

        List<Expenses> expensesList = expensesRepository.findByCategory_GroupId(groupId);
        
        // Calculate the total amount
        BigDecimal totalAmount = expensesList.stream()
            .map(Expenses::getAmount) // Extract the amount from each expense
            .reduce(BigDecimal.ZERO, BigDecimal::add); // Sum up the amounts

        // Return the total amount as a double
        return totalAmount.doubleValue();
    }
    

    public String deleteExpense(Long expenseId) {
        Optional<Expenses> expenseOpt = expensesRepository.findById(expenseId);
        if (expenseOpt.isEmpty()) {
            return "❌ Expense not found!";
        }
        expensesRepository.delete(expenseOpt.get());
        return "✅ Expense deleted successfully!";
    }

    public List<ExpensesDetails> getExpensesByGroupId(Long groupId) {
        List<Expenses> expenses = expensesRepository.findAllByCategoryIdGroupId(groupId);
        // Map the Expenses entities to the ExpensesResponse DTO
        List<ExpensesDetails> response = expenses.stream()
            .map(e -> new  ExpensesDetails(
                e.getId(),
                e.getDate(),
                e.getAmount(),
                e.getCategory().getId().getName(),
                e.getUser().getUsername(),
                e.getStatus(),
                e.getDescription()
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

    public Map<String, BigDecimal> getUserExpenses(String phone, String filter) {

        List<Expenses> expenses = expensesRepository.findAllByUser_Phone(phone);

        TimeGroup groupBy = parseTimeGroup(filter);
        return getUserExpensesGroupedBy(expenses, groupBy);


    }


    public Map<String, BigDecimal> getUserExpensesGroupedBy(List<Expenses> expenses, TimeGroup groupBy) {
        if (expenses == null || expenses.isEmpty()) {
            System.out.println("⚠️ No expenses provided.");
            return Map.of();
        }
    
        return expenses.stream()
            .filter(e -> e != null && e.getDate() != null)
            .collect(Collectors.groupingBy(
                e -> {
                    Date date = e.getDate();
                    LocalDate localDate;
    
                    if (date instanceof java.sql.Date sqlDate) {
                        localDate = sqlDate.toLocalDate();
                    } else {
                        localDate = date.toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate();
                    }
    
                    return switch (groupBy) {
                        case DAY -> localDate.toString();
                        case MONTH -> localDate.getYear() + "-" + String.format("%02d", localDate.getMonthValue());
                        case YEAR -> String.valueOf(localDate.getYear());
                    };
                },
                Collectors.mapping(
                    Expenses::getAmount,
                    Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                )
            ));
    }
    
    

    public TimeGroup parseTimeGroup(String input) {
        try {
            return TimeGroup.valueOf(input.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid time group: " + input +
                ". Valid values are: DAY, MONTH, YEAR");
        }
    }


    public List<ExpensesSummaryDTO> getTodaysExpenses(String phone) {
        Date today = new Date();
        List<Long> groups = participantsRepository.findGroupIdsByUserPhone(phone);
        List<Expenses> allExpenses = new ArrayList<>();

        for (Long groupId : groups) {
            List<Expenses> temporary_expenses = expensesRepository.findAllByCategoryIdGroupIdAndDate(groupId, today);
            allExpenses.addAll(temporary_expenses);
        }

        return allExpenses.stream()
                .map(e -> new ExpensesSummaryDTO(
                    e.getDate(),
                    e.getAmount(),
                    e.getCategory().getId().getName()
                ))
                .collect(Collectors.toList());

    }

    public ExpensesDetails getExpense(Long expenseId) {
        Expenses expense = expensesRepository.findById(expenseId).orElse(null);
        if (expense == null) {
            return null;
        }
        return new ExpensesDetails(
            expense.getId(),
            expense.getDate(),
            expense.getAmount(),
            expense.getCategory().getId().getName(),
            expense.getUser().getUsername(),
            expense.getStatus(),
            expense.getDescription()
        );
    }

    public List<ExpensesDetails> getExpensesByDate(String date, String phone) {
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date new_date ;
        try {
            new_date = formatter.parse(date);
            List<Expenses> expenses = expensesRepository.findAllByUser_PhoneAndDate(phone, new_date);
            return expenses.stream()
            .map(e -> new ExpensesDetails(
                e.getId(),
                e.getDate(),
                e.getAmount(),
                e.getCategory().getId().getName(),
                e.getUser().getUsername(),
                e.getStatus(),
                e.getDescription()
            ))
            .collect(Collectors.toList());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        
       

    
        
    }
    

}