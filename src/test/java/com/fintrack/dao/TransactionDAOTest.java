package com.fintrack.dao;

import com.fintrack.db.ConnectionProvider;
import com.fintrack.exceptions.DataAccessException;
import com.fintrack.model.MonthlyTransaction;
import com.fintrack.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionDAOTest {

    private static final String URL = System.getenv().getOrDefault(
            "FINTRACK_TEST_DB_URL", "jdbc:mysql://localhost:3307/fintrack_test");
    private static final String USER = "root";
    private static final String PASSWORD = "mysql";

    private final ConnectionProvider testConnections =
            () -> DriverManager.getConnection(URL, USER, PASSWORD);

    private TransactionDAO dao;

    @BeforeEach
    void setUp() throws SQLException {
        assertTrue(URL.contains("fintrack_test"),
                "Tests must never run against the real database");
        try (Connection connection = testConnections.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM transactions");
        }
        dao = new TransactionDAO(testConnections);
    }

    @Test
    void save_newTransaction_assignsGeneratedId() {
        Transaction saved = dao.save(expense("Groceries", "150.50"));

        assertNotNull(saved.getId());
    }

    @Test
    void findById_returnsWhatWasSaved() {
        Transaction saved = dao.save(expense("Groceries", "150.50"));

        Optional<Transaction> found = dao.findById(saved.getId());

        assertTrue(found.isPresent());
        Transaction transaction = found.get();
        assertEquals("Groceries", transaction.getDescription());
        assertEquals(new BigDecimal("150.50"), transaction.getAmount());
        assertFalse(transaction.isIncome());
        assertEquals(LocalDate.of(2026, 10, 29), transaction.getDate());
    }

    @Test
    void save_monthlyTransaction_keepsTypeAndDayOfMonth() {
        Transaction saved = dao.save(new MonthlyTransaction(
                "Salary", new BigDecimal("5000.00"), true, LocalDate.of(2026, 10, 5), 5));

        Transaction found = dao.findById(saved.getId()).orElseThrow();

        MonthlyTransaction monthly = assertInstanceOf(MonthlyTransaction.class, found);
        assertEquals(5, monthly.getDayOfMonth());
        assertTrue(monthly.isIncome());
    }

    @Test
    void findAll_returnsEverySavedTransaction() {
        dao.save(expense("Rent", "1200.00"));
        dao.save(expense("Internet", "99.90"));

        assertEquals(2, dao.findAll().size());
    }

    @Test
    void save_existingTransaction_updatesTheRow() {
        Transaction saved = dao.save(expense("Groceries", "150.50"));
        saved.setDescription("Supermarket");

        dao.save(saved);

        assertEquals("Supermarket",
                dao.findById(saved.getId()).orElseThrow().getDescription());
        assertEquals(1, dao.findAll().size());
    }

    @Test
    void save_unknownId_throwsDataAccessException() {
        Transaction ghost = expense("Ghost", "10.00");
        ghost.setId(999_999L);

        assertThrows(DataAccessException.class, () -> dao.save(ghost));
    }

    @Test
    void deleteById_removesTheTransaction() {
        Transaction saved = dao.save(expense("Groceries", "150.50"));

        assertTrue(dao.deleteById(saved.getId()));
        assertTrue(dao.findById(saved.getId()).isEmpty());
    }

    @Test
    void deleteById_unknownId_returnsFalse() {
        assertFalse(dao.deleteById(999_999L));
    }

    @Test
    void findById_unknownId_returnsEmpty() {
        assertTrue(dao.findById(999_999L).isEmpty());
    }

    private Transaction expense(String description, String amount) {
        return new Transaction(description, new BigDecimal(amount), false, LocalDate.of(2026, 10, 29));
    }
}