package com.bank.dao;

import com.bank.config.DatabaseConnection;
import com.bank.model.Transaction;
import com.bank.repository.TransactionRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao implements TransactionRepository {

    public List<Transaction> findByAccountId(String accountId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            return findByAccountId(accountId, connection);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find transactions", e);
        }
    }

    public List<Transaction> findByAccountId(String accountId, Connection connection) {
        String sql = """
                SELECT transaction_id, account_id, type, amount, datetime
                FROM transactions
                WHERE account_id = ?
                ORDER BY datetime DESC
                """;

        List<Transaction> transactions = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                transactions.add(new Transaction(
                        resultSet.getString("transaction_id"),
                        resultSet.getString("account_id"),
                        resultSet.getString("type"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("datetime")
                ));
            }

            return transactions;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find transactions", e);
        }
    }

    public void add(Transaction transaction) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            add(transaction, connection);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add transaction", e);
        }
    }

    public void add(Transaction transaction, Connection connection) {
        String sql = """
                INSERT INTO transactions (transaction_id, account_id, type, amount, datetime)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, transaction.getTransactionId());
            statement.setString(2, transaction.getAccountId());
            statement.setString(3, transaction.getType());
            statement.setDouble(4, transaction.getAmount());
            statement.setString(5, transaction.getDatetime());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add transaction", e);
        }
    }

    public int findMaxTransactionNumber() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            return findMaxTransactionNumber(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find latest transaction ID", e);
        }
    }

    public int findMaxTransactionNumber(Connection connection) {
        String sql = """
                SELECT COALESCE(MAX(CAST(SUBSTR(transaction_id, 2) AS INTEGER)), 0) AS max_id
                FROM transactions
                WHERE transaction_id LIKE 'T%'
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? resultSet.getInt("max_id") : 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find latest transaction ID", e);
        }
    }
}
