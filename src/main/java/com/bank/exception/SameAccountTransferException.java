package com.bank.exception;

public class SameAccountTransferException extends RuntimeException {
    public SameAccountTransferException() {
        super("Cannot transfer to the same account");
    }
}
