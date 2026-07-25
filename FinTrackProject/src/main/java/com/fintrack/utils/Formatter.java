package com.fintrack.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Formatter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String formatDate(LocalDate date){
        return date.format(DATE_FORMATTER);
    }

    public static String formatAmount(double amount){
        return String.format("%.2f", amount);
    }

    public static String formatSign(boolean isIncome){
        return isIncome ? "+" : "-";
    }
}
