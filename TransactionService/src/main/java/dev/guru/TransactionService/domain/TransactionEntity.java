package dev.guru.TransactionService.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class TransactionEntity {
    @Id
    private Long transactionId;
    @NotNull(message = "Id cannot be empty")
    private Long senderId;
    @NotNull(message = "Id cannot be empty")
    private Long receiverId;
    @Positive(message = "Amount cannot be negative")
    private Currency currency;
    @NotNull(message = "Amount cannot be negative")
    private BigDecimal amount;
    private Instant transactionDate;
    private TransactionStatus transactionStatus;

    public TransactionEntity() {}

    public TransactionEntity(Builder builder){
        this.transactionId = builder.transactionId;
        this.senderId = builder.senderId;
        this.receiverId = builder.receiverId;
        this.currency = builder.currency;
        this.amount = builder.amount;
        this.transactionDate = builder.transactionDate;
        this.transactionStatus = builder.transactionStatus;
    }

    public static class Builder{
        private final Long transactionId;
        private Long senderId;
        private Long receiverId;
        private Currency currency;
        private BigDecimal amount;
        private Instant transactionDate;
        private TransactionStatus transactionStatus;

        public Builder(Long transactionId){
            this.transactionId = transactionId;
        }

        public Builder withSenderId(Long senderId){
            this.senderId = senderId;
            return this;
        }

        public Builder withReceiverId(Long receiverId){
            this.receiverId = receiverId;
            return this;
        }

        public Builder withCurrency(Currency currency){
            this.currency = currency;
            return this;
        }

        public Builder withAmount(BigDecimal amount){
            this.amount = amount;
            return this;
        }

        public Builder withTransactionDate(Instant transactionDate){
            this.transactionDate = transactionDate;
            return this;
        }

        public Builder withTransactionStatus(TransactionStatus transactionStatus){
            this.transactionStatus = transactionStatus;
            return this;
        }

        public TransactionEntity build(){
            return new TransactionEntity(this);
        }
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public Instant getTransactionDate() {
        return transactionDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}
