package com.bank.dao;

import com.bank.config.DatabaseConnection;
import com.bank.model.Account;
import com.bank.repository.AccountRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDao implements AccountRepository {

    public Account findByUserId(String userId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            return findByUserId(userId, connection);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find account", e);
        }
    }

    public String findUserIdByUsernameUserNumber(String usernameUserNumber, Connection connection) {
        String sql = """
                SELECT user_id
                FROM users
                WHERE username = ? OR user_number = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usernameUserNumber);
            statement.setString(2, usernameUserNumber);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("user_id");
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find account", e);
        }
    }

    public Account findByUserId(String userId, Connection connection) {
        String sql = """
                SELECT account_id, user_id, balance
                FROM accounts
                WHERE user_id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Account(
                        resultSet.getString("account_id"),
                        resultSet.getString("user_id"),
                        resultSet.getDouble("balance")
                );
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find account", e);
        }
    }

    public void update(Account account) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            update(account, connection);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update account", e);
        }
    }

    public void update(Account account, Connection connection) {
        String sql = """
                UPDATE accounts
                SET balance = ?
                WHERE account_id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, account.getBalance());
            statement.setString(2, account.getAccountId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update account", e);
        }
    }
}
