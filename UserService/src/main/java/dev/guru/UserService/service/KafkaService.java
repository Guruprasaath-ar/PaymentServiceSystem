package dev.guru.UserService.service;

import dev.guru.UserService.dto.UserResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    private final KafkaTemplate<String, UserResponse> kafkaTemplate;

    @Autowired
    public KafkaService(KafkaTemplate<String, UserResponse> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void fireEvent(UserResponse userResponse) {
        kafkaTemplate.send("userCreated",userResponse);
    }

}
