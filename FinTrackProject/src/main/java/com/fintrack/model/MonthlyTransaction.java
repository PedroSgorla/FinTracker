package com.fintrack.model;

import com.fintrack.exceptions.InvalidInputException;

import java.time.LocalDate;

public class MonthlyTransaction extends Transaction {

    private int dayOfMonth;

    public MonthlyTransaction(String description, double amount, boolean isIncome, LocalDate date, int dayOfMonth) throws InvalidInputException {
        super(description, amount, isIncome, date);
        if (dayOfMonth < 1 || dayOfMonth > 31) {
            throw new InvalidInputException("Invalid day of Month. Day must be between 1 and 31!");
        }
        this.dayOfMonth = dayOfMonth;

    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    @Override
    public String getType() {
        return "Monthly";
    }
}
