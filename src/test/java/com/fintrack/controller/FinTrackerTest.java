package com.fintrack.controller;

import com.fintrack.exceptions.InvalidInputException;
import com.fintrack.model.MonthlyTransaction;
import com.fintrack.model.Transaction;
import com.fintrack.repository.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FinTrackerTest {

    private static final LocalDate DATE = LocalDate.of(2026, 10, 29);

    private FinTracker finTracker;

    @BeforeEach
    void setUp() {
        finTracker = new FinTracker(new InMemoryRepository<>());
    }

    @Test
    void addTransaction_assignsId() {
        Transaction saved = finTracker.addTransaction(expense("Rent", "1200"));

        assertNotNull(saved.getId());
        assertEquals(1, finTracker.getTransactions().size());
    }

    @Test
    void totals_sumIncomeAndExpensesCorrectly() {
        finTracker.addTransaction(income("Salary", "1000"));
        finTracker.addTransaction(expense("Groceries", "250.50"));
        finTracker.addTransaction(expense("Transport", "49.50"));

        assertEquals(new BigDecimal("1000.00"), finTracker.calculateTotalIncome());
        assertEquals(new BigDecimal("300.00"), finTracker.calculateTotalExpenses());
        assertEquals(new BigDecimal("700.00"), finTracker.calculateTotalBalance());
    }

    @Test
    void totals_withNoTransactions_areZero() {
        assertEquals(0, finTracker.calculateTotalBalance().compareTo(BigDecimal.ZERO));
    }

    @Test
    void filterByType_income_returnsOnlyIncome() {
        finTracker.addTransaction(income("Salary", "1000"));
        finTracker.addTransaction(expense("Rent", "400"));

        List<Transaction> result = finTracker.filterByType(true);

        assertEquals(1, result.size());
        assertEquals("Salary", result.get(0).getDescription());
    }

    @Test
    void filterByType_expenses_returnsOnlyExpenses() {
        finTracker.addTransaction(income("Salary", "1000"));
        finTracker.addTransaction(expense("Rent", "400"));

        List<Transaction> result = finTracker.filterByType(false);

        assertEquals(1, result.size());
        assertEquals("Rent", result.get(0).getDescription());
    }

    @Test
    void filterByMonthly_returnsOnlyMonthlyTransactions() {
        finTracker.addTransaction(expense("Coffee", "5"));
        finTracker.addTransaction(new MonthlyTransaction("Rent", new BigDecimal("400"), false, DATE, 10));

        List<Transaction> result = finTracker.filterByMonthly();

        assertEquals(1, result.size());
        assertEquals("Rent", result.get(0).getDescription());
    }

    @Test
    void searchTransaction_ignoresCase() {
        finTracker.addTransaction(expense("Supermarket", "80"));

        assertEquals(1, finTracker.searchTransaction("SUPER").size());
        assertTrue(finTracker.searchTransaction("pharmacy").isEmpty());
    }

    @Test
    void searchTransaction_blankKeyword_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> finTracker.searchTransaction("  "));
    }

    @Test
    void removeTransaction_existingId_removesIt() {
        Transaction saved = finTracker.addTransaction(expense("Rent", "400"));

        finTracker.removeTransaction(saved.getId());

        assertTrue(finTracker.getTransactions().isEmpty());
    }

    @Test
    void removeTransaction_unknownId_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> finTracker.removeTransaction(99L));
    }

    @Test
    void updateTransaction_unsavedTransaction_throwsInvalidInputException() {
        Transaction unsaved = expense("Rent", "400");

        assertThrows(InvalidInputException.class, () -> finTracker.updateTransaction(unsaved));
    }

    private Transaction income(String description, String amount) {
        return new Transaction(description, new BigDecimal(amount), true, DATE);
    }

    private Transaction expense(String description, String amount) {
        return new Transaction(description, new BigDecimal(amount), false, DATE);
    }
}