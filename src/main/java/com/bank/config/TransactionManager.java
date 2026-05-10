package com.bank.config;

public interface TransactionManager {
    void executeInTransaction(DatabaseTransaction transaction);
}
