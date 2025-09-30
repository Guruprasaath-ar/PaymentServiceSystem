package dev.guru.TransactionService.controller;

import dev.guru.TransactionService.customExceptions.TransactionNotFoundException;
import dev.guru.TransactionService.customExceptions.UnsupportedCurrencyTypeException;
import dev.guru.TransactionService.domain.TransactionEntity;
import dev.guru.TransactionService.dto.TransactionRequest;
import dev.guru.TransactionService.dto.TransactionResponse;
import dev.guru.TransactionService.service.KafkaService;
import dev.guru.TransactionService.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final KafkaService kafkaService;

    public TransactionController(TransactionService transactionService, KafkaService kafkaService) {
        this.transactionService = transactionService;
        this.kafkaService = kafkaService;
    }

    @PostMapping("/")
    public ResponseEntity<?> registerTransaction(@Valid @RequestBody TransactionRequest transactionRequest) throws UnsupportedCurrencyTypeException {
        if(!transactionService.validateTransactionRequest(transactionRequest))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        TransactionEntity transactionEntity = transactionService.createTransaction(transactionRequest);
        TransactionResponse transactionResponse = transactionService.convertTransactionToTransactionResponse(transactionEntity,"transaction completed successfully");
        kafkaService.sendMessage("transactionTopic",transactionResponse);
        return new ResponseEntity<>(transactionResponse, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<?> refundTransaction(@PathVariable Long id) throws Exception {
        TransactionResponse response = transactionService.refundTransaction(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllTransactions(@RequestParam(required = false,defaultValue = "5") int pageSize,
                                                @RequestParam(required = false,defaultValue = "1") int pageNumber,
                                                @RequestParam(required = false,defaultValue = "transactionId" ) String sortBy,
                                                @RequestParam(required = false,defaultValue = "ASC") String sortDir,
                                                @RequestParam(required = false) Long senderId) throws Exception {
        Sort sort = sortDir.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        List<TransactionResponse> transactions = transactionService.getTransactions(PageRequest.of(pageNumber-1,pageSize,sort),senderId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable Long id) throws TransactionNotFoundException {
        TransactionResponse response = transactionService.getTransactionById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
