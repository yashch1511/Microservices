package com.ecommerce.user_service.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.ecommerce.user_service.event.UserRegisteredEvent;

@Configuration
public class KafkaConfig {

    @Bean
    public ProducerFactory<String, UserRegisteredEvent> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put("bootstrap.servers", "localhost:9092");
        configProps.put("key.serializer", StringSerializer.class);
        configProps.put("value.serializer", JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}