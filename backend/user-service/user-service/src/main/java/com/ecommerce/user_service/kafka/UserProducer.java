package com.ecommerce.user_service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ecommerce.user_service.event.UserRegisteredEvent;

@Service
public class UserProducer {

    private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;

    public UserProducer(KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserRegisteredEvent(UserRegisteredEvent event) {

        kafkaTemplate.send("user-registrations", event);

        System.out.println("User registration event sent: " + event);

    }

}