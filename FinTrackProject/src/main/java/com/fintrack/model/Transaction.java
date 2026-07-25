package com.fintrack.model;

import java.time.LocalDate;
import com.fintrack.utils.Formatter;

/**
 * Represents a single regular financial transaction (income or expense).
 * <p>
 * Serves as the base class for more specific transaction types, such as
 * {@link MonthlyTransaction}.
 */
public class Transaction {

    /** Free-text description of the transaction. */
    private String description;

    /** Monetary value of the transaction. Expected to be greater than 0. */
    private double amount;

    /** {@code true} if this transaction is income, {@code false} if it is an expense. */
    private boolean isIncome;

    /** Date on which the transaction occurred. */
    private LocalDate date;

    /**
     * Creates a new regular transaction.
     *
     * @param description free-text description of the transaction
     * @param amount monetary value of the transaction
     * @param isIncome {@code true} if income, {@code false} if expense
     * @param date date the transaction occurred
     */
    public Transaction(String description, double amount, boolean isIncome, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.isIncome = isIncome;
        this.date = date;
    }

    /**
     * Returns the transaction description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Updates the transaction description.
     *
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the transaction amount.
     *
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Updates the transaction amount.
     *
     * @param amount the new amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Indicates whether this transaction is income.
     *
     * @return {@code true} if income, {@code false} if expense
     */
    public boolean isIncome() {
        return isIncome;
    }

    /**
     * Updates whether this transaction is income.
     *
     * @param isIncome {@code true} if income, {@code false} if expense
     */
    public void isIncome(boolean isIncome) {
        this.isIncome = isIncome;
    }

    /**
     * Returns the date the transaction occurred.
     *
     * @return the transaction date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Updates the date the transaction occurred.
     *
     * @param date the new transaction date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns the type label of this transaction.
     * <p>
     * Subclasses override this to identify themselves (e.g. "Monthly").
     *
     * @return the type label, {@code "Regular"} for this base class
     */
    public String getType(){
        return "Regular";
    }

    /**
     * Returns a human-readable representation of this transaction, including
     * its type, description, signed amount, and formatted date.
     *
     * @return the formatted string representation
     */
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
