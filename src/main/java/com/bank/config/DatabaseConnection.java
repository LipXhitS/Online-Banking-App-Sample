package com.bank.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DEFAULT_URL = "jdbc:sqlite:bank.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(System.getProperty("bank.db.url", DEFAULT_URL));
    }
}
