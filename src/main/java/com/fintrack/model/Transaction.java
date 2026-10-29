package com.fintrack.model;

import java.time.LocalDate;

import com.fintrack.exceptions.InvalidInputException;
import com.fintrack.utils.Formatter;

public class Transaction {

    private Long id;
    private String description;
    private double amount;
    private boolean income;
    private LocalDate date;

    public Transaction(String description, double amount, boolean income, LocalDate date) {
        this.description = requireDescription(description);
        this.amount = requireAmount(amount);
        this.income = income;
        this.date = requireDate(date);
    }
    public Long getId(){return id; }

    public void setId(Long id){ this.id = id; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = requireDescription(description);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = requireAmount(amount);
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
        this.date = requireDate(date);
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

    private static String requireDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new InvalidInputException("Description cannot be empty!");
        }
        return description.trim();
    }

    private static double requireAmount(double amount) {
        if (amount <= 0) {
            throw new InvalidInputException("Amount must be greater than 0!");
        }
        return amount;
    }

    private static LocalDate requireDate(LocalDate date) {
        if (date == null) {
            throw new InvalidInputException("Date cannot be null!");
        }
        return date;
    }
}
