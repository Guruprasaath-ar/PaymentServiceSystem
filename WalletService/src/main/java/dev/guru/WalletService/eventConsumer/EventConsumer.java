package dev.guru.WalletService.eventConsumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.guru.WalletService.dto.TransactionRequest;
import dev.guru.WalletService.dto.TransactionResponse;
import dev.guru.WalletService.dto.UserResponse;
import dev.guru.WalletService.service.WalletManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventConsumer {
    private final WalletManagerService walletManagerService;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String,TransactionResponse>  kafkaTemplate;

    @Autowired
    public EventConsumer(WalletManagerService walletManagerService,
                         ObjectMapper objectMapper,
                         KafkaTemplate<String,TransactionResponse> kafkaTemplate) {
        this.walletManagerService = walletManagerService;
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "userCreated", groupId = "wallet-service-group")
    public void handleUserCreatedEvent(String message){
        try {

            UserResponse userResponse = objectMapper.readValue(message, UserResponse.class);
            if (userResponse.isResult())
                walletManagerService.createWallet(userResponse.getUserId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "transactionRequestTopic", groupId = "wallet-service-group")
    public void handleTransactionRequestEvent(String message) {
        try{
            TransactionRequest transactionRequest = objectMapper.readValue(message, TransactionRequest.class);
            TransactionResponse transactionResponse = walletManagerService.handleTransaction(transactionRequest);
            kafkaTemplate.send("transactionProcessed", transactionResponse);
        }
        catch (JsonProcessingException e){
            e.printStackTrace();
        }
    }
}
