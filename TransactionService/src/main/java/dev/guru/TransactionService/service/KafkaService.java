package dev.guru.TransactionService.service;

import dev.guru.TransactionService.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    KafkaTemplate<String, TransactionResponse> kafkaTemplate;

    @Autowired
    public KafkaService(KafkaTemplate<String, TransactionResponse> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic,TransactionResponse transactionResponse){
        kafkaTemplate.send(topic,transactionResponse);
    }

}
