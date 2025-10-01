package dev.guru.TransactionService.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.guru.TransactionService.dto.TransactionResponse;
import dev.guru.TransactionService.service.KafkaService;
import dev.guru.TransactionService.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EventConsumer {

    private final ObjectMapper mapper;
    private final KafkaService kafkaService;

    @Autowired
    public EventConsumer(ObjectMapper mapper, KafkaService kafkaService, TransactionService transactionService) {
        this.mapper = mapper;
        this.kafkaService = kafkaService;
    }

    @KafkaListener(topics = "transactionProcessed",groupId = "transaction-service-group")
    public ResponseEntity<?> processTransaction(String message) {
        try{
            TransactionResponse transactionResponse = mapper.readValue(message, TransactionResponse.class);
            kafkaService.fireTransactionCompletedEventToNotify("transactionResponseTopic",transactionResponse);
            return new ResponseEntity<>(transactionResponse, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
