package com.fintrack.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utility methods for formatting transaction data (dates, amounts, and
 * income/expense signs) for display.
 */
public class Formatter {

    /** Formatter used to render dates as {@code dd/MM/yyyy}. */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Formats a date as {@code dd/MM/yyyy}.
     *
     * @param date the date to format
     * @return the formatted date string
     */
    public static String formatDate(LocalDate date){
        return date.format(DATE_FORMATTER);
    }

    /**
     * Formats an amount with exactly two decimal places.
     *
     * @param amount the amount to format
     * @return the formatted amount string
     */
    public static String formatAmount(double amount){
        return String.format("%.2f", amount);
    }

    /**
     * Returns the sign character representing an income or expense.
     *
     * @param isIncome {@code true} for income, {@code false} for expense
     * @return {@code "+"} if income, {@code "-"} if expense
     */
    public static String formatSign(boolean isIncome){
        return isIncome ? "+" : "-";
    }
}
