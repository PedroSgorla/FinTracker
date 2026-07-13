package com.fintrack.model;

import java.time.LocalDate;

public class Transaction {

    private String description;
    private double amount;
    private boolean isIncome;
    private LocalDate date;

    public Transaction(String description, double amount, boolean isIncome, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.isIncome = isIncome;
        this.date = date;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public boolean getIsIncome() {
        return isIncome;
    }
    public void setIsIncome(boolean isIncome) {
        this.isIncome = isIncome;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getType(){
        return "Regular ";
    }

    @Override
    public String toString() {
        String sign = isIncome ? "+" : "-";
        return String.format("[%s] %s | %s%.2f | %s", getType(), getDescription(), sign, amount, date);
    }
}
