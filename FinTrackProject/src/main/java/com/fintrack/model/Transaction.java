package com.fintrack.model;

import java.time.LocalDate;
import com.fintrack.utils.Formatter;

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
    public boolean isIncome() {
        return isIncome;
    }
    public void isIncome(boolean isIncome) {
        this.isIncome = isIncome;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getType(){
        return "Regular";
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s%s | %s",
                getType(),
                description,
                Formatter.formatSign(isIncome),
                Formatter.formatAmount(amount),
                Formatter.formatDate(date));
    }
}
