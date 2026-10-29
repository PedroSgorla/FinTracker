package com.fintrack.controller;

import com.fintrack.exceptions.InvalidInputException;
import com.fintrack.model.Transaction;
import com.fintrack.model.TransactionType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FinTracker {

    private final List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction transaction) {
        Objects.requireNonNull(transaction, "Transaction cannot be null");
        transactions.add(transaction);
    }

    public Transaction removeTransaction(int index) {
        if (index < 1 || index > transactions.size()) {
            throw new InvalidInputException("Invalid index: " + index);
        }
        return transactions.remove(index - 1);
    }

    public List<Transaction> filterByType(boolean income) {
        return transactions.stream()
                .filter(transaction -> transaction.isIncome() == income)
                .toList();
    }

    public List<Transaction> filterByMonthly() {
        return transactions.stream()
                .filter(transaction -> transaction.getType() == TransactionType.MONTHLY)
                .toList();
    }

    public List<Transaction> searchTransaction(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new InvalidInputException("Keyword cannot be empty!");
        }
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        return transactions.stream()
                .filter(transaction -> transaction.getDescription()
                        .toLowerCase(Locale.ROOT)
                        .contains(normalizedKeyword))
                .toList();
    }

    public BigDecimal calculateTotalIncome() {
        return transactions.stream()
                .filter(Transaction::isIncome)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public BigDecimal calculateTotalExpenses() {
        return transactions.stream()
                .filter(transaction -> !transaction.isIncome())
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalBalance() {
        return calculateTotalIncome().subtract(calculateTotalExpenses());
    }

    public List<Transaction> getTransactions() {
        return List.copyOf(transactions);
    }
}