package dev.guru.TransactionService.customExceptions;

public class IllegalCurrencyException extends Exception {
    public IllegalCurrencyException() {
    }
    public IllegalCurrencyException(String message) {
        super(message);
    }
}
