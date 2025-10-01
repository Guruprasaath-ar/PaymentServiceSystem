package dev.guru.WalletService.service;

import dev.guru.WalletService.domain.TransactionStatus;
import dev.guru.WalletService.domain.Wallet;
import dev.guru.WalletService.dto.TransactionRequest;
import dev.guru.WalletService.dto.TransactionResponse;
import dev.guru.WalletService.repository.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletManagerService {

    private final WalletRepo walletRepo;

    @Autowired
    public WalletManagerService(WalletRepo walletRepo) {
        this.walletRepo = walletRepo;
    }

    public Wallet createWallet(Long id) {
        Wallet wallet = new Wallet
                .Builder()
                .withUserId(id)
                .withBalance(BigDecimal.valueOf(0))
                .build();
        return walletRepo.save(wallet);
    }

    public TransactionResponse handleTransaction(TransactionRequest transactionRequest) {
        TransactionResponse transactionResponse = null;
        if(transactionRequest == null){
             transactionResponse = new TransactionResponse
                    .Builder()
                    .withTransactionStatus(TransactionStatus.FAILURE)
                    .withMessage("Invalid Transaction Request")
                    .build();
             return transactionResponse;
        }

        Wallet senderWallet = walletRepo.findByUserId(transactionRequest.getSenderId()).orElse(null);
        Wallet receiverWallet = walletRepo.findByUserId(transactionRequest.getReceiverId()).orElse(null);

        if(senderWallet == null || receiverWallet == null){
            transactionResponse = new TransactionResponse
                    .Builder()
                    .withTransactionStatus(TransactionStatus.FAILURE)
                    .withMessage("Invalid sender or receiver wallet")
                    .build();
            return transactionResponse;
        }

        if(senderWallet.getBalance().compareTo(transactionRequest.getAmount()) < 0){
            transactionResponse = new TransactionResponse
                    .Builder()
                    .withTransactionStatus(TransactionStatus.FAILURE)
                    .withMessage("Insufficient funds")
                    .build();
            return transactionResponse;
        }

        senderWallet.debit(transactionRequest.getAmount());
        receiverWallet.credit(transactionRequest.getAmount());
        walletRepo.save(senderWallet);
        walletRepo.save(receiverWallet);

        transactionResponse = new TransactionResponse
                .Builder()
                .withTransactionStatus(TransactionStatus.SUCCESS)
                .withMessage("Transaction Success")
                .build();
        return transactionResponse;
    }

}
