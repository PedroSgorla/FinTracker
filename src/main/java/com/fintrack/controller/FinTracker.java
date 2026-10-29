package com.fintrack.controller;

import com.fintrack.exceptions.InvalidInputException;
import com.fintrack.model.Transaction;
import com.fintrack.model.TransactionType;
import com.fintrack.repository.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FinTracker {

    private final Repository<Transaction, Long> repository;

    public FinTracker(Repository<Transaction, Long> repository) {
        this.repository = Objects.requireNonNull(repository, "Repository cannot be null");
    }

    public Transaction addTransaction(Transaction transaction) {
        Objects.requireNonNull(transaction, "Transaction cannot be null");
        return repository.save(transaction);
    }

    public Transaction updateTransaction(Transaction transaction) {
        Objects.requireNonNull(transaction, "Transaction cannot be null");
        if (transaction.getId() == null) {
            throw new InvalidInputException("Transaction has not been saved yet!");
        }
        return repository.save(transaction);
    }

    public void removeTransaction(Long id) {
        if (!repository.deleteById(id)) {
            throw new InvalidInputException("No transaction with id " + id);
        }
    }

    public List<Transaction> getTransactions() {
        return repository.findAll();
    }

    public List<Transaction> filterByType(boolean income) {
        return getTransactions().stream()
                .filter(transaction -> transaction.isIncome() == income)
                .toList();
    }

    public List<Transaction> filterByMonthly() {
        return getTransactions().stream()
                .filter(transaction -> transaction.getType() == TransactionType.MONTHLY)
                .toList();
    }

    public List<Transaction> searchTransaction(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new InvalidInputException("Keyword cannot be empty!");
        }
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        return getTransactions().stream()
                .filter(transaction -> transaction.getDescription()
                        .toLowerCase(Locale.ROOT)
                        .contains(normalizedKeyword))
                .toList();
    }

    public BigDecimal calculateTotalIncome() {
        return getTransactions().stream()
                .filter(Transaction::isIncome)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalExpenses() {
        return getTransactions().stream()
                .filter(transaction -> !transaction.isIncome())
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalBalance() {
        return getTransactions().stream()
                .map(transaction -> transaction.isIncome()
                        ? transaction.getAmount()
                        : transaction.getAmount().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}