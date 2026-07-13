package com.fintrack.model;

import java.time.LocalDate;

public class MonthlyTransaction extends Transaction {

    private int dayOfMonth;

    public MonthlyTransaction(String description, double amount, boolean isIncome, LocalDate date, int dayOfMonth) {
        super(description, amount, isIncome, date);
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
        return "Monthly ";
    }
}
