package dev.guru.TransactionService.dto;

import dev.guru.TransactionService.domain.Currency;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class TransactionRequest {

    @NotNull(message = "Sender Id cannot be empty")
    private Long senderId;
    @NotNull(message = "Receiver Id cannot be empty")
    private Long receiverId;

    private Currency currency;
    @Positive(message = "Amount cannot be negative")
    private BigDecimal amount;

    public TransactionRequest() {

    }

    public Long getSenderId() {
        return senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
