package dev.guru.TransactionService.customExceptions;

public class TransactionNotFoundException extends RuntimeException {
    private final Long id;

    public TransactionNotFoundException(Long id) {
        super("Transaction with id " + id + " not found");
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}