package com.bank.model;

public class Account {
    private final String accountID;
    private double balance;

    public Account(String accountID, double balance) {
        this.accountID = accountID;
        this.balance = balance;
    }

    public String accountIDGetter() {
        return this.accountID;
    }

    public double balanceGetter() {
        return this.balance;
    }

    public void balanceSetter(double balance) {
        this.balance = balance;
    }
}