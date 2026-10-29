package com.fintrack.model;

import com.fintrack.exceptions.InvalidInputException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionTest {

    private static final LocalDate DATE = LocalDate.of(2026, 10, 29);

    @Test
    void constructor_validData_createsTransaction() {
        Transaction transaction = new Transaction("Rent", new BigDecimal("1200"), false, DATE);

        assertEquals("Rent", transaction.getDescription());
        assertEquals(new BigDecimal("1200.00"), transaction.getAmount());
        assertFalse(transaction.isIncome());
        assertEquals(DATE, transaction.getDate());
        assertEquals(TransactionType.REGULAR, transaction.getType());
        assertNull(transaction.getId());
    }

    @Test
    void constructor_descriptionWithExtraSpaces_isTrimmed() {
        Transaction transaction = new Transaction("  Rent ", BigDecimal.TEN, false, DATE);

        assertEquals("Rent", transaction.getDescription());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    void constructor_blankDescription_throwsInvalidInputException(String description) {
        assertThrows(InvalidInputException.class,
                () -> new Transaction(description, BigDecimal.TEN, true, DATE));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-0.01", "-100"})
    void constructor_nonPositiveAmount_throwsInvalidInputException(String amount) {
        assertThrows(InvalidInputException.class,
                () -> new Transaction("Rent", new BigDecimal(amount), true, DATE));
    }

    @Test
    void constructor_nullAmount_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> new Transaction("Rent", null, true, DATE));
    }

    @Test
    void constructor_nullDate_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> new Transaction("Rent", BigDecimal.TEN, true, null));
    }

    @Test
    void setAmount_roundsToTwoDecimals() {
        Transaction transaction = new Transaction("Rent", BigDecimal.TEN, true, DATE);

        transaction.setAmount(new BigDecimal("2.345"));

        assertEquals(new BigDecimal("2.35"), transaction.getAmount());
    }

    @Test
    void setAmount_invalidValue_throwsAndKeepsPreviousAmount() {
        Transaction transaction = new Transaction("Rent", new BigDecimal("50"), true, DATE);

        assertThrows(InvalidInputException.class,
                () -> transaction.setAmount(BigDecimal.ZERO));

        assertEquals(new BigDecimal("50.00"), transaction.getAmount());
    }
}
