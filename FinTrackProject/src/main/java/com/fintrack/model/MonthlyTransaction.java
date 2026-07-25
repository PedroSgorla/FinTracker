package com.fintrack.model;

import com.fintrack.exceptions.InvalidInputException;

import java.time.LocalDate;

/**
 * Represents a transaction that recurs on a fixed day of every month
 * (e.g. rent, a subscription, or a salary payment).
 */
public class MonthlyTransaction extends Transaction {

    /** Day of the month (1-31) on which this transaction recurs. */
    private int dayOfMonth;

    /**
     * Creates a new monthly transaction.
     *
     * @param description free-text description of the transaction
     * @param amount monetary value of the transaction
     * @param isIncome {@code true} if income, {@code false} if expense
     * @param date date the transaction occurred
     * @param dayOfMonth day of the month it recurs on, between 1 and 31
     * @throws InvalidInputException if {@code dayOfMonth} is not between 1 and 31
     */
    public MonthlyTransaction(String description, double amount, boolean isIncome, LocalDate date, int dayOfMonth) throws InvalidInputException {
        super(description, amount, isIncome, date);
        if (dayOfMonth < 1 || dayOfMonth > 31) {
            throw new InvalidInputException("Invalid day of Month. Day must be between 1 and 31!");
        }
        this.dayOfMonth = dayOfMonth;

    }

    /**
     * Returns the day of the month this transaction recurs on.
     *
     * @return the day of month, between 1 and 31
     */
    public int getDayOfMonth() {
        return dayOfMonth;
    }

    /**
     * Updates the day of the month this transaction recurs on.
     *
     * @param dayOfMonth the new day of month
     */
    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    /**
     * Returns the type label for this transaction.
     *
     * @return {@code "Monthly"}
     */
    @Override
    public String getType() {
        return "Monthly";
    }
}
