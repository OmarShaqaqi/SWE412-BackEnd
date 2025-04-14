package com.backend.senior_backend.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpensesSummaryDTO {
    private Date date;
    private BigDecimal amount;
    private String categoryName;
}

