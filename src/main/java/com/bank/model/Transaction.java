package com.bank.model;

public class Transaction {
    private final String transactionId;
    private final String accountId;
    private String type;
    private double amount;
    private String datetime;

    public Transaction(String transactionId, String accountId, String type, double amount, String datetime) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.datetime = datetime;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDatetime() {
        return this.datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
