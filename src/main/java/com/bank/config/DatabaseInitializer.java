package com.bank.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() {
        try (
                Connection connection = DatabaseConnection.getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.execute("PRAGMA foreign_keys = ON");

            statement.execute("""
                    CREATE TABLE IF NOT EXISTS users (
                        user_id TEXT PRIMARY KEY,
                        username TEXT NOT NULL UNIQUE,
                        user_number TEXT NOT NULL UNIQUE,
                        pin INTEGER NOT NULL CHECK (pin BETWEEN 10000 AND 99999)
                    )
                    """);

            statement.execute("""
                    CREATE TABLE IF NOT EXISTS accounts (
                        account_id TEXT PRIMARY KEY,
                        user_id TEXT NOT NULL UNIQUE,
                        balance REAL NOT NULL DEFAULT 0,
                        FOREIGN KEY (user_id) REFERENCES users(user_id)
                    )
                    """);

            statement.execute("""
                    CREATE TABLE IF NOT EXISTS transactions (
                        transaction_id TEXT PRIMARY KEY,
                        account_id TEXT NOT NULL,
                        type TEXT NOT NULL,
                        amount REAL NOT NULL,
                        datetime TEXT NOT NULL,
                        FOREIGN KEY (account_id) REFERENCES accounts(account_id)
                    )
                    """);

            seedSampleData(statement);

            System.out.println("Database initialized");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    private static void seedSampleData(Statement statement) throws SQLException {
        statement.execute("""
                INSERT OR IGNORE INTO users (user_id, username, user_number, pin) 
                VALUES 
                    ('1', 'jgrinham0', '09864485596', 75551),
                    ('2', 'hmuscott1', '09076872853', 81104),
                    ('3', 'bburwin2', '09172142695', 88006),
                    ('4', 'hsatchel3', '09380251350', 78818),
                    ('5', 'tfevier4', '09937002768', 52667),
                    ('6', 'cvansaltsberg5', '09341633849', 37164),
                    ('7', 'bfison6', '09328758185', 85355),
                    ('8', 'aleschelle7', '09064376260', 11188),
                    ('9', 'mreading8', '09404031474', 56159),
                    ('10', 'smacklin9', '09244772637', 70727)
                """);

        statement.execute("""
                INSERT OR IGNORE INTO accounts (account_id, user_id, balance)
                VALUES
                    ('A0001', '1', 5000.00),
                    ('A0002', '2', 3500.00),
                    ('A0003', '3', 7200.00),
                    ('A0004', '4', 1800.00),
                    ('A0005', '5', 9600.00),
                    ('A0006', '6', 2400.00),
                    ('A0007', '7', 4100.00),
                    ('A0008', '8', 6700.00),
                    ('A0009', '9', 1250.00),
                    ('A0010', '10', 8900.00)
                """);

        statement.execute("""
                INSERT OR IGNORE INTO transactions (transaction_id, account_id, type, amount, datetime)
                VALUES
                    ('T0001', 'A0001', 'Cash In', 5000.00, datetime('now')),
                    ('T0002', 'A0002', 'Cash In', 3500.00, datetime('now')),
                    ('T0003', 'A0003', 'Cash In', 7200.00, datetime('now')),
                    ('T0004', 'A0004', 'Cash In', 1800.00, datetime('now')),
                    ('T0005', 'A0005', 'Cash In', 9600.00, datetime('now')),
                    ('T0006', 'A0006', 'Cash In', 2400.00, datetime('now')),
                    ('T0007', 'A0007', 'Cash In', 4100.00, datetime('now')),
                    ('T0008', 'A0008', 'Cash In', 6700.00, datetime('now')),
                    ('T0009', 'A0009', 'Cash In', 1250.00, datetime('now')),
                    ('T0010', 'A0010', 'Cash In', 8900.00, datetime('now'))
                """);
    }
}
