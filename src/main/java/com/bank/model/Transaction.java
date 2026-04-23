package com.bank.model;

public class Transaction {
    private final String transactionID;
    private String type;
    private double amount;
    private String datetime;

    public Transaction(String transactionID, String type, double amount, String datetime) {
        this.transactionID = transactionID;
        this.type = type;
        this.amount = amount;
        this.datetime = datetime;
    }

    public String transactionIDGetter() {
        return this.transactionID;
    }

    public String typeGetter() {
        return this.type;
    }

    public void typeSetter(String type) {
        this.type = type;
    }

    public double amount() {
        return this.amount;
    }

    public void amountSetter(double amount) {
        this.amount = amount;
    }

    public String datetimeGetter() {
        return this.datetime;
    }

    public void datetimeSetter(String datetime) {
        this.datetime = datetime;
    }
}