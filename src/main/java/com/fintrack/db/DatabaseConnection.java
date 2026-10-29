package com.fintrack.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {

    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3307/fintrack";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "mysql";

    private DatabaseConnection(){}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                setting("FINTRACK_DB_URL", DEFAULT_URL),
                setting("FINTRACK_DB_USER", DEFAULT_USER),
                setting("FINTRACK_DB_PASSWORD", DEFAULT_PASSWORD));
    }

    private static String setting(String name, String defaultValue){
        String value = System.getenv(name);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }
}
