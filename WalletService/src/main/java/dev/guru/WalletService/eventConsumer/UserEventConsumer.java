package dev.guru.WalletService.eventConsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.guru.WalletService.dto.UserResponse;
import dev.guru.WalletService.service.WalletManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventConsumer {
    private final WalletManagerService walletManagerService;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserEventConsumer(WalletManagerService walletManagerService, ObjectMapper objectMapper){
        this.walletManagerService = walletManagerService;
        this.objectMapper = objectMapper;
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
}
