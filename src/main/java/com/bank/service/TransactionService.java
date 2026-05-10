package com.bank.service;

import com.bank.model.Transaction;
import com.bank.repository.TransactionRepository;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getTransactions(String accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    public void logTransaction(String accountId, String type, double amount) {
        String transactionId = generateTransactionId();

        Transaction transaction = new Transaction(
                transactionId,
                accountId,
                type,
                amount,
                LocalDateTime.now().toString()
        );
        this.transactionRepository.add(transaction);
    }

    public void logTransaction(String accountId, String type, double amount, Connection connection) {
        String transactionId = generateTransactionId(connection);

        Transaction transaction = new Transaction(
                transactionId,
                accountId,
                type,
                amount,
                LocalDateTime.now().toString()
        );
        this.transactionRepository.add(transaction, connection);
    }

    private String generateTransactionId() {
        return String.format("T%04d", this.transactionRepository.findMaxTransactionNumber() + 1);
    }

    private String generateTransactionId(Connection connection) {
        return String.format("T%04d", this.transactionRepository.findMaxTransactionNumber(connection) + 1);
    }
}
