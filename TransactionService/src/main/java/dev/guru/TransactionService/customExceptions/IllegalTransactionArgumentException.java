package dev.guru.TransactionService.customExceptions;

public class IllegalTransactionArgumentException extends RuntimeException {
    private final Long id;

    public IllegalTransactionArgumentException(Long id) {
        super("Transaction with id " + id + " not found");
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}