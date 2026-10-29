package com.fintrack.model;

import com.fintrack.exceptions.InvalidInputException;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MonthlyTransaction extends Transaction {

    private int dayOfMonth;

    public MonthlyTransaction(String description, BigDecimal amount, boolean isIncome, LocalDate date, int dayOfMonth) throws InvalidInputException {
        super(description, amount, isIncome, date);
        this.dayOfMonth = requireDayOfMonth(dayOfMonth);
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = requireDayOfMonth(dayOfMonth);
    }

    @Override
    public TransactionType getType() {
        return TransactionType.MONTHLY;
    }

    private static int requireDayOfMonth(int dayOfMonth) {
        if (dayOfMonth < 1 || dayOfMonth > 31) {
            throw new InvalidInputException("Invalid day of month. Day must be between 1 and 31!");
        }
        return dayOfMonth;
    }
}
