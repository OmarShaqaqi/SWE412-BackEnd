package com.backend.senior_backend.dto;

import lombok.Data;
@Data
public class ParticipantInformationDTO {

    private String phone;
    private String username;
    private double totalExpense;

    public ParticipantInformationDTO(String phone, String username, double totalExpense) {
        this.phone = phone;
        this.username = username;
        this.totalExpense = totalExpense;
    }
    

}
