package dev.guru.TransactionService.service;

import dev.guru.TransactionService.dto.TransactionRequest;
import dev.guru.TransactionService.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    KafkaTemplate<String, TransactionResponse> kafkaTransactionResponseTemplate;
    KafkaTemplate<String, TransactionRequest> kafkaTransactionRequestTemplate;

    @Autowired
    public KafkaService(
            @Qualifier("transactionResponseKafkaTemplate")
            KafkaTemplate<String, TransactionResponse> responseTemplate,
            @Qualifier("transactionRequestKafkaTemplate")
            KafkaTemplate<String, TransactionRequest> requestTemplate) {
        this.kafkaTransactionResponseTemplate = responseTemplate;
        this.kafkaTransactionRequestTemplate = requestTemplate;
    }

    public void fireTransactionCompletedEventToNotify(String topic, TransactionResponse transactionResponse){
        kafkaTransactionResponseTemplate.send(topic,transactionResponse);
    }

    public void fireTransactionInitiatedEventToNotify(String topic, TransactionRequest transactionRequest){
        kafkaTransactionRequestTemplate.send(topic,transactionRequest);
    }
}
