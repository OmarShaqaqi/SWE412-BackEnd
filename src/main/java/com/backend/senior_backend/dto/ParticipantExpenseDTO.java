package com.backend.senior_backend.dto;

public class ParticipantExpenseDTO {

    private String phone;
    private double totalExpense;
    private boolean isLeader;

    // Constructor
    public ParticipantExpenseDTO(String phone, double totalExpense, boolean isLeader) {
        this.phone = phone;
        this.totalExpense = totalExpense;
        this.isLeader = isLeader;
    }

    // Getters and setters
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public boolean isLeader() {
        return isLeader;
    }

    public void setLeader(boolean leader) {
        isLeader = leader;
    }
}

