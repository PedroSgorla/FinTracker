package com.fintrack.model;

import java.time.LocalDate;
import com.fintrack.utils.Formatter;

public class Transaction {

    private Long id;
    private String description;
    private double amount;
    private boolean income;
    private LocalDate date;

    public Transaction(String description, double amount, boolean income, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.income = income;
        this.date = date;
    }
    public Long getId(){return id; }

    public void setId(Long id){ this.id = id; }

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
        return income;
    }

    public void setIncome(boolean income) {
        this.income = income;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TransactionType getType(){
        return TransactionType.REGULAR;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s%s | %s",
                getType(),
                description,
                Formatter.formatSign(income),
                Formatter.formatAmount(amount),
                Formatter.formatDate(date));
    }
}
