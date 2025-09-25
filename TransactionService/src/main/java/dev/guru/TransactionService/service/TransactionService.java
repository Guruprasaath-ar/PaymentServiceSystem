package dev.guru.TransactionService.service;

import dev.guru.TransactionService.customExceptions.IllegalTransactionArgumentException;
import dev.guru.TransactionService.customExceptions.InvalidRefundException;
import dev.guru.TransactionService.domain.Currency;
import dev.guru.TransactionService.domain.TransactionEntity;
import dev.guru.TransactionService.domain.TransactionStatus;
import dev.guru.TransactionService.dto.TransactionRequest;
import dev.guru.TransactionService.dto.TransactionResponse;
import dev.guru.TransactionService.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TransactionService {

    private TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionEntity createTransaction(TransactionRequest transactionRequest) {
        TransactionEntity entity = new TransactionEntity.Builder()
                .withSenderId(transactionRequest.getSenderId())
                .withReceiverId(transactionRequest.getReceiverId())
                .withCurrency(transactionRequest.getCurrency())
                .withAmount(transactionRequest.getAmount())
                .withTransactionStatus(TransactionStatus.SUCCESS)
                .build();
        transactionRepository.save(entity);
        return entity;
    }

    public TransactionResponse convertTransactionToTransactionResponse(TransactionEntity transactionEntity,String message){
        return new TransactionResponse
                .Builder()
                .withTransactionId(transactionEntity.getTransactionId())
                .withTransactionStatus(transactionEntity.getTransactionStatus())
                .withMessage(message)
                .build();
    }

    public boolean validateTransactionRequest(TransactionRequest transactionRequest) {
        Currency currency = transactionRequest.getCurrency();
        if(currency.equals(Currency.USD) || currency.equals(Currency.EUR) || currency.equals(Currency.INR) || currency.equals(Currency.GBP)) {
            return true;
        }

        throw new IllegalArgumentException();
    }

    public Optional<TransactionEntity> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public TransactionResponse refundTransaction(Long transactionId) throws Exception {
        TransactionEntity transactionEntity = transactionRepository.findById(transactionId).orElse(null);
        if(transactionEntity == null)
            throw new IllegalTransactionArgumentException(transactionId);
        else if(transactionEntity.getTransactionStatus() == TransactionStatus.REFUNDED)
            throw new InvalidRefundException("Transaction is already refunded");
        else if(transactionEntity.getTransactionStatus() != TransactionStatus.SUCCESS)
            throw new InvalidRefundException("Invalid Transaction Id");
        transactionEntity.setTransactionStatus(TransactionStatus.REFUNDED);
        transactionRepository.save(transactionEntity);
        return convertTransactionToTransactionResponse(transactionEntity,"Transaction has been refunded successfully");
    }

    public List<TransactionEntity> getTransactions() {
        return transactionRepository.findAll();
    }
}
