package com.fintrack.dao;

import com.fintrack.db.ConnectionProvider;
import com.fintrack.db.DatabaseConnection;
import com.fintrack.exceptions.DataAccessException;
import com.fintrack.model.MonthlyTransaction;
import com.fintrack.model.Transaction;
import com.fintrack.model.TransactionType;
import com.fintrack.repository.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionDAO implements Repository<Transaction, Long> {

    private static final String COLUMNS =
            "id, description, amount, is_income, transaction_date, type, day_of_month";

    private static final String INSERT = """
            INSERT INTO transactions
                (description, amount, is_income, transaction_date, type, day_of_month)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

    private static final String UPDATE = """
            UPDATE transactions
            SET description = ?, amount = ?, is_income = ?,
                transaction_date = ?, type = ?, day_of_month = ?
            WHERE id = ?
            """;

    private static final String SELECT_ALL =
            "SELECT " + COLUMNS + " FROM transactions ORDER BY transaction_date, id";

    private static final String SELECT_BY_ID =
            "SELECT " + COLUMNS + " FROM transactions WHERE id = ?";

    private static final String DELETE = "DELETE FROM transactions WHERE id = ?";

    private final ConnectionProvider connections;

    public TransactionDAO(ConnectionProvider connections) {
        this.connections = connections;
    }

    public TransactionDAO() {
        this(DatabaseConnection::getConnection);
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transaction.getId() == null ? insert(transaction) : update(transaction);
    }

    private Transaction insert(Transaction transaction) {
        try (Connection connection = connections.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            bind(statement, transaction);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    transaction.setId(keys.getLong(1));
                }
            }
            return transaction;
        } catch (SQLException e) {
            throw new DataAccessException("Could not insert transaction", e);
        }
    }

    private Transaction update(Transaction transaction) {
        try (Connection connection = connections.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            bind(statement, transaction);
            statement.setLong(7, transaction.getId());
            if (statement.executeUpdate() == 0) {
                throw new DataAccessException(
                        "No transaction with id " + transaction.getId(), null);
            }
            return transaction;
        } catch (SQLException e) {
            throw new DataAccessException("Could not update transaction", e);
        }
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        try (Connection connection = connections.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(mapRow(resultSet)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not find transaction " + id, e);
        }
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = connections.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                transactions.add(mapRow(resultSet));
            }
            return transactions;
        } catch (SQLException e) {
            throw new DataAccessException("Could not list transactions", e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (Connection connection = connections.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Could not delete transaction " + id, e);
        }
    }

    private void bind(PreparedStatement statement, Transaction transaction) throws SQLException {
        statement.setString(1, transaction.getDescription());
        statement.setBigDecimal(2, transaction.getAmount());
        statement.setBoolean(3, transaction.isIncome());
        statement.setObject(4, transaction.getDate());
        statement.setString(5, transaction.getType().name());
        if (transaction instanceof MonthlyTransaction monthly) {
            statement.setInt(6, monthly.getDayOfMonth());
        } else {
            statement.setNull(6, Types.TINYINT);
        }
    }
    
    private Transaction mapRow(ResultSet resultSet) throws SQLException {
        String description = resultSet.getString("description");
        BigDecimal amount = resultSet.getBigDecimal("amount");
        boolean income = resultSet.getBoolean("is_income");
        LocalDate date = resultSet.getObject("transaction_date", LocalDate.class);
        TransactionType type = TransactionType.valueOf(resultSet.getString("type"));

        Transaction transaction = switch (type) {
            case REGULAR -> new Transaction(description, amount, income, date);
            case MONTHLY -> new MonthlyTransaction(
                    description, amount, income, date, resultSet.getInt("day_of_month"));
        };
        transaction.setId(resultSet.getLong("id"));
        return transaction;
    }
}
