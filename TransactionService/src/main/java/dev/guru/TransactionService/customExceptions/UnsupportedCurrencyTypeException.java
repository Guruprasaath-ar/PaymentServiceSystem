package dev.guru.TransactionService.customExceptions;

public class UnsupportedCurrencyTypeException extends Exception {
    public UnsupportedCurrencyTypeException() {
    }
    public UnsupportedCurrencyTypeException(String message) {
        super(message);
    }
}
