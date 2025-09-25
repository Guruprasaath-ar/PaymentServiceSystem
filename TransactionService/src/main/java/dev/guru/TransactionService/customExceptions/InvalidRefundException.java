package dev.guru.TransactionService.customExceptions;

public class InvalidRefundException extends Exception {
    public InvalidRefundException(String message) {
        super(message);
    }
}
