package com.backend.senior_backend.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExpensesResponse {
    private Long id;
    private Date date;
    private BigDecimal amount;
    private String status;
    private String categoryName;
    

    // Default constructor
    public ExpensesResponse() {}

    // Constructor with fields
    public ExpensesResponse(Long id, Date date, BigDecimal amount, String status, String categoryName) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.status = status;
        this.categoryName = categoryName;
    }

   
}
