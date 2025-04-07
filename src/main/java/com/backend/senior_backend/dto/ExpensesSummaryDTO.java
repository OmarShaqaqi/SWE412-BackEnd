package com.backend.senior_backend.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class ExpensesSummaryDTO {
    private Date date;
    private BigDecimal amount;
    private String categoryName;

    public ExpensesSummaryDTO(Date date, BigDecimal amount, String categoryName) {
        this.date = date;
        this.amount = amount;
        this.categoryName = categoryName;
    }

    // Getters and setters
}

