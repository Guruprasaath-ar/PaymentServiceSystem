package dev.guru.TransactionService.config;

import dev.guru.TransactionService.constant.AppConstant;
import dev.guru.TransactionService.dto.TransactionRequest;
import dev.guru.TransactionService.dto.TransactionResponse;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean(name = "transactionRequestTopic")
    public NewTopic transactionTopic() {
        return TopicBuilder
                .name(AppConstant.TRANSACTION_REQUEST_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean(name = "transactionResponseTopic")
    public NewTopic transactionResponseTopic() {
        return TopicBuilder
                .name(AppConstant.TRANSACTION_RESPONSE_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean(name = "producerConfig")
    public Map<String, Object> producerConfigs() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return configProps;
    }

    @Bean(name = "producerTransactionResponse")
    public ProducerFactory<String, TransactionResponse> producerTransactionResponseFactory(@Qualifier("producerConfig") Map<String, Object> producerConfigs) {
        return new DefaultKafkaProducerFactory<>(producerConfigs);
    }

    @Bean(name = "producerTransactionRequest")
    public ProducerFactory<String, TransactionRequest> producerTransactionRequestFactory(@Qualifier("producerConfig") Map<String, Object> producerConfigs) {
        return new DefaultKafkaProducerFactory<>(producerConfigs);
    }

    @Bean(name = "transactionResponseKafkaTemplate")
    public KafkaTemplate<String, TransactionResponse> transactionResponseKafkaTemplate(@Qualifier("producerTransactionResponse") ProducerFactory<String, TransactionResponse> factory) {
        return new KafkaTemplate<>(factory);
    }

    @Bean(name = "transactionRequestKafkaTemplate")
    public KafkaTemplate<String, TransactionRequest> transactionRequestKafkaTemplate(@Qualifier("producerTransactionRequest") ProducerFactory<String, TransactionRequest> factory) {
        return new KafkaTemplate<>(factory);
    }
}
