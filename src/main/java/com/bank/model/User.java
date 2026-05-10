package com.bank.model;

public class User {
    private final String userId;
    private String username;
    private String userNumber;
    private int userPin;

    public User(String userId, String username, String userNumber, int userPin) {
        this.userId = userId;
        this.username = username;
        this.userNumber = userNumber;
        this.userPin = userPin;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public boolean matchesLogin(String input) {
        return input != null && (input.equals(username) || input.equals(userNumber));
    }

    public boolean validatePin(int inputPin) {
        return this.userPin == inputPin;
    }

    public void changePin(int oldPin, int newPin) {
        if (!validatePin(oldPin)) {
            throw new RuntimeException("Invalid current PIN");
        }
        this.userPin = newPin;
    }
}
