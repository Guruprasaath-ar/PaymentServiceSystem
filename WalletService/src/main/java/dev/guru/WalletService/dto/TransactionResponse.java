package dev.guru.WalletService.dto;

import dev.guru.WalletService.domain.TransactionStatus;

public class TransactionResponse {
    private Long transactionId;
    private TransactionStatus transactionStatus;
    private String message;

    public TransactionResponse(){

    }

    public TransactionResponse(Builder builder){
        this.transactionId = builder.transactionId;
        this.transactionStatus=builder.transactionStatus;
        this.message=builder.message;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }
    public String getMessage() {
        return message;
    }
    public Long getTransactionId() {
        return transactionId;
    }

    public static class Builder{
        private TransactionStatus transactionStatus;
        private String message;
        private Long transactionId;

        public Builder(){}

        public Builder withTransactionId(Long transactionId){
            this.transactionId=transactionId;
            return this;
        }

        public Builder withTransactionStatus(TransactionStatus transactionStatus){
            this.transactionStatus = transactionStatus;
            return this;
        }

        public Builder withMessage(String message){
            this.message = message;
            return this;
        }

        public TransactionResponse build(){
            return new TransactionResponse(this);
        }
    }
}
