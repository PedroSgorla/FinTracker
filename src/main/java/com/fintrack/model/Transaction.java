package com.fintrack.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import com.fintrack.exceptions.InvalidInputException;
import com.fintrack.utils.Formatter;

public class Transaction {

    private Long id;
    private String description;
    private BigDecimal amount;
    private boolean income;
    private LocalDate date;

    public Transaction(String description, BigDecimal amount, boolean income, LocalDate date) {
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
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

    private static BigDecimal requireAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new InvalidInputException("Amount must be greater than 0!");
        }
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    private static LocalDate requireDate(LocalDate date) {
        if (date == null) {
            throw new InvalidInputException("Date cannot be null!");
        }
        return date;
    }
}
