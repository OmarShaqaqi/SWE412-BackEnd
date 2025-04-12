package com.backend.senior_backend.dto;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
@Data
public class ExpensesDetails {
    private Long id;
    private Date date;
    private BigDecimal amount;
    private String categoryName;
    private String actor;   
    private String status;
    private String description;

    public ExpensesDetails(Long id ,Date date, BigDecimal amount, String categoryName, String actor, String status, String description) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.categoryName = categoryName;
        this.actor = actor;
        this.status = status;
        this.description = description;
    }


}
