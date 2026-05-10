package com.bank.config;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTransactionManager implements TransactionManager {

    public void executeInTransaction(DatabaseTransaction transaction) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                transaction.execute(connection);
                connection.commit();
            } catch (RuntimeException | SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database transaction failed", e);
        }
    }
}
