package com.backend.senior_backend.controllers;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.backend.senior_backend.dto.ExpensesSummaryDTO;

import com.backend.senior_backend.dto.ExpenseRequest;
import com.backend.senior_backend.dto.ExpensesResponse;
import com.backend.senior_backend.models.Expenses;
import com.backend.senior_backend.service.ExpensesService;
import com.backend.senior_backend.service.ExpensesService.TimeGroup;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpensesService expenseService;

    // @PostMapping("/add")
    // public ResponseEntity<String> addExpense(
    //         @RequestParam Long groupId,
    //         @RequestParam String categoryName,
    //         @RequestParam BigDecimal amount,
    //         @RequestParam String description) {
        
    //     String phone = SecurityContextHolder.getContext().getAuthentication().getName();
    //     java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
    //     String response = expenseService.addExpense(groupId, categoryName, phone, amount, sqlDate, description);
    //     return ResponseEntity.ok(response);
    // }
    @PostMapping("/add")
    public ResponseEntity<String> addExpense(@RequestBody ExpenseRequest request) {
        String phone = SecurityContextHolder.getContext().getAuthentication().getName();
        String response = expenseService.addExpense(
                request.getGroupId(),
                request.getCategoryName(),
                phone,
                request.getAmount(),
                request.getDate() == null ?  new java.sql.Date(new Date().getTime()) : request.getDate(),
                
                request.getDescription()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Expenses>> getExpenses(@RequestParam Long groupId, @RequestParam String categoryName) {
        List<Expenses> expenses = expenseService.getExpenses(groupId, categoryName);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/list/{groupId}")
    public ResponseEntity<List<ExpensesResponse>> getExpenses(@PathVariable Long groupId) {
        List<ExpensesResponse> expenses = expenseService.getExpensesByGroupId(groupId);
        return ResponseEntity.ok(expenses);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteExpense(@RequestParam Long expenseId) {
        String response = expenseService.deleteExpense(expenseId);
        return ResponseEntity.ok(response);
    }

     // ✅ Approve an expense
    @PostMapping("/approve")
    public ResponseEntity<String> approveExpense(@RequestParam Long expenseId) {
        System.out.println("Expense ID: " + expenseId);
        String leaderPhone = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean success = expenseService.updateExpenseStatus(expenseId, "APPROVED", leaderPhone);

        return success ? ResponseEntity.ok("Expense approved") :
                ResponseEntity.badRequest().body("Approval failed");
    }

    // ✅ Reject an expense
    @PostMapping("/reject")
    public ResponseEntity<String> rejectExpense(@RequestParam Long expenseId) {
        String leaderPhone = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean success = expenseService.updateExpenseStatus(expenseId, "REJECTED", leaderPhone);

        return success ? ResponseEntity.ok("Expense rejected") :
                ResponseEntity.badRequest().body("Rejection failed");
    }

    @GetMapping("/{filter}")
    public Map<String,BigDecimal> getExpensesByPeriod(@PathVariable String filter) {

        String phone = SecurityContextHolder.getContext().getAuthentication().getName();

        System.out.println("Filter: " + filter);
        return expenseService.getUserExpenses(phone,filter);


    }

    @GetMapping("/today")
    public List<ExpensesSummaryDTO> getTodaysExpenses() {
        String phone = SecurityContextHolder.getContext().getAuthentication().getName();

        return expenseService.getTodaysExpenses(phone);

    }
    
    
}
