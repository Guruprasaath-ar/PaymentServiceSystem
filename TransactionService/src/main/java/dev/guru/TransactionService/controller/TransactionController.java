package dev.guru.TransactionService.controller;

import dev.guru.TransactionService.customExceptions.IllegalTransactionArgumentException;
import dev.guru.TransactionService.domain.TransactionEntity;
import dev.guru.TransactionService.dto.TransactionRequest;
import dev.guru.TransactionService.dto.TransactionResponse;
import dev.guru.TransactionService.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/")
    public ResponseEntity<?> registerTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        if(!transactionService.validateTransactionRequest(transactionRequest))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        TransactionEntity transactionEntity = transactionService.createTransaction(transactionRequest);
        TransactionResponse transactionResponse = transactionService.convertTransactionToTransactionResponse(transactionEntity,"transaction completed successfully");
        return new ResponseEntity<>(transactionResponse, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<?> refundTransaction(@PathVariable Long id) throws Exception {
        TransactionResponse response = transactionService.refundTransaction(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllTransactions() {
        List<TransactionEntity> transactions = transactionService.getTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public TransactionEntity getTransactionById(@PathVariable Long id) throws IllegalTransactionArgumentException {
        return transactionService.getTransactionById(id)
                .orElseThrow(() -> new IllegalTransactionArgumentException(id));
    }
}
