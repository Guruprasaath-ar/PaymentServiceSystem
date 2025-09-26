package dev.guru.TransactionService.service;

import dev.guru.TransactionService.customExceptions.TransactionNotFoundException;
import dev.guru.TransactionService.customExceptions.InvalidRefundException;
import dev.guru.TransactionService.customExceptions.UnsupportedCurrencyTypeException;
import dev.guru.TransactionService.domain.Currency;
import dev.guru.TransactionService.domain.TransactionEntity;
import dev.guru.TransactionService.domain.TransactionStatus;
import dev.guru.TransactionService.dto.TransactionRequest;
import dev.guru.TransactionService.dto.TransactionResponse;
import dev.guru.TransactionService.repository.TransactionRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final List<Currency> SUPPORTED_CURRENCIES = new ArrayList<>(List.of(Currency.USD, Currency.EUR, Currency.GBP, Currency.INR));

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

    public boolean validateTransactionRequest(TransactionRequest transactionRequest) throws UnsupportedCurrencyTypeException {
        Currency currency = transactionRequest.getCurrency();
        if(SUPPORTED_CURRENCIES.contains(currency))
            return true;
        throw new UnsupportedCurrencyTypeException();
    }

    public TransactionResponse getTransactionById(Long id) {
        TransactionEntity entity = transactionRepository.findById(id).orElse(null);
        if(entity == null)
            throw new TransactionNotFoundException(id);

        return convertTransactionToTransactionResponse(entity,entity.getTransactionStatus().toString());
    }

    public TransactionResponse refundTransaction(Long transactionId) throws Exception {
        TransactionEntity transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));
        transaction.refund();
        transactionRepository.save(transaction);
        return convertTransactionToTransactionResponse(transaction,"Transaction has been refunded successfully");
    }

    public List<TransactionResponse> getTransactions(Pageable pageRequest) {
        List<TransactionEntity> transactionEntities = transactionRepository.findAll(pageRequest).getContent();
        List<TransactionResponse> responses = new ArrayList<>();
        for(TransactionEntity transactionEntity : transactionEntities) {
            if(transactionEntity == null)
                continue;
            String message = transactionEntity.getTransactionStatus().toString();
            responses.add(convertTransactionToTransactionResponse(transactionEntity,message));
        }
        return responses;
    }
}
