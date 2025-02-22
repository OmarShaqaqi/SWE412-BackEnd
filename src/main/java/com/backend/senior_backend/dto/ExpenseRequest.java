package com.backend.senior_backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ExpenseRequest {
    private Long groupId;
    private String categoryName;
    private BigDecimal amount;
    private String description;
}
