package com.ecommerce.notification_service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.ecommerce.notification_service.event.UserRegisteredEvent;
@Component
public class KafkaConsumer {

    @KafkaListener(topics = "user-registrations", groupId = "notification-group")
    public void consume(UserRegisteredEvent event) {

        System.out.println("=================================");
        System.out.println("USER REGISTRATION EVENT RECEIVED");
        System.out.println("User ID: " + event.getUserId());
        System.out.println("Name: " + event.getName());
        System.out.println("Email: " + event.getEmail());
        System.out.println("Sending welcome email...");
        System.out.println("=================================");

    }

}