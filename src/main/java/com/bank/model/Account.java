package com.bank.model;

public class Account {
    private final String accountId;
    private final String userId;
    private double balance;

    public Account(String accountId, String userId, double balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public String getUserId() {
        return this.userId;
    }

    public double getBalance() {
        return this.balance;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public void withdraw(double amount) {
        this.balance -= amount;
    }
}
