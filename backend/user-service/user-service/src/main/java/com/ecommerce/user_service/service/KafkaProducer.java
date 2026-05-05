package com.ecommerce.user_service.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ecommerce.user_service.event.UserRegisteredEvent;
import com.ecommerce.user_service.model.User;


@Service
public class KafkaProducer {

    private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserRegisteredEvent(User user) {
        // convert User to UserRegisteredEvent
       UserRegisteredEvent event = new UserRegisteredEvent(user.getId(), user.getName(), user.getEmail());

        kafkaTemplate.send("user-registrations", event)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("✅ UserRegisteredEvent sent to Kafka for user: " + user.getEmail());
                } else {
                    System.out.println("❌ Kafka error: " + ex.getMessage());
                }
            });
    }
}