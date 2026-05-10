package com.bank.repository;

import com.bank.model.Transaction;

import java.sql.Connection;
import java.util.List;

public interface TransactionRepository {
    List<Transaction> findByAccountId(String accountId);

    List<Transaction> findByAccountId(String accountId, Connection connection);

    void add(Transaction transaction);

    void add(Transaction transaction, Connection connection);

    int findMaxTransactionNumber();

    int findMaxTransactionNumber(Connection connection);
}
