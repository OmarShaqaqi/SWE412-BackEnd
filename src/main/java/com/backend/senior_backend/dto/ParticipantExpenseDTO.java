package com.backend.senior_backend.dto;



@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class ParticipantExpenseDTO {

    private String phone;
    private double totalExpense;
    private boolean isLeader;
    private String image;

    
}

