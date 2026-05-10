package com.bank.dao;

import com.bank.config.DatabaseConnection;
import com.bank.model.User;
import com.bank.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao implements UserRepository {

    public User findByLogin(String input) {
        String sql = """
                SELECT user_id, username, user_number, pin
                FROM users
                WHERE (username = ? OR user_number = ?)
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, input);
            statement.setString(2, input);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new User(
                        resultSet.getString("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("user_number"),
                        resultSet.getInt("pin")
                );
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user", e);
        }
    }
}
