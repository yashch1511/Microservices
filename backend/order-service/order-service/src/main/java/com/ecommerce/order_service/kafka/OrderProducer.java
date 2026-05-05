package com.ecommerce.order_service.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ecommerce.order_service.event.OrderPlacedEvent;

@Service
public class OrderProducer {

    @Autowired
    private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    private static final String TOPIC = "order-placed-topic";

    public void sendOrderPlacedEvent(OrderPlacedEvent event) {
        kafkaTemplate.send(TOPIC, event).whenComplete((result, ex) -> {
            if (ex != null) {
                System.out.println("OrderPlacedEvent failed to send: " + ex.getMessage());
            } else {
                System.out.println("OrderPlacedEvent sent: " + event);
            }
        });
    }
}
