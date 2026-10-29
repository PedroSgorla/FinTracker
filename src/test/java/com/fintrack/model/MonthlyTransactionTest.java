package com.fintrack.model;

import com.fintrack.exceptions.InvalidInputException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MonthlyTransactionTest {

    private static final LocalDate DATE = LocalDate.of(2026, 10, 5);

    @Test
    void constructor_validData_createsMonthlyTransaction() {
        MonthlyTransaction transaction =
                new MonthlyTransaction("Salary", new BigDecimal("5000"), true, DATE, 5);

        assertEquals(TransactionType.MONTHLY, transaction.getType());
        assertEquals(5, transaction.getDayOfMonth());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 31})
    void constructor_boundaryDays_areAccepted(int day) {
        MonthlyTransaction transaction =
                new MonthlyTransaction("Rent", BigDecimal.TEN, false, DATE, day);

        assertEquals(day, transaction.getDayOfMonth());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 32})
    void constructor_invalidDay_throwsInvalidInputException(int day) {
        assertThrows(InvalidInputException.class,
                () -> new MonthlyTransaction("Rent", BigDecimal.TEN, false, DATE, day));
    }

    @Test
    void setDayOfMonth_invalidDay_throwsAndKeepsPreviousDay() {
        MonthlyTransaction transaction =
                new MonthlyTransaction("Rent", BigDecimal.TEN, false, DATE, 10);

        assertThrows(InvalidInputException.class, () -> transaction.setDayOfMonth(40));

        assertEquals(10, transaction.getDayOfMonth());
    }
}