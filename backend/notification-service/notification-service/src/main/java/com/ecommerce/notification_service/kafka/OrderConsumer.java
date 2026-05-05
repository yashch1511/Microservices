package com.ecommerce.notification_service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ecommerce.notification_service.event.OrderPlacedEvent;

@Service
public class OrderConsumer {

    public OrderConsumer() {
        System.out.println("OrderConsumer bean created successfully...");
    }

    @KafkaListener(
        topics = "order-placed-topic",
        groupId = "notification-order-group",
        containerFactory = "orderKafkaListenerContainerFactory"
    )
    public void consume(OrderPlacedEvent event) {
        System.out.println("Received OrderPlacedEvent: " + event);
        // Add notification logic here
    }
}