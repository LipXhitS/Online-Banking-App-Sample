package com.bank.config;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface DatabaseTransaction {
    void execute(Connection connection) throws SQLException;
}
