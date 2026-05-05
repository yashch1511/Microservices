package com.ecommerce.payment_service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ecommerce.payment_service.event.OrderPlacedEvent;
import com.ecommerce.payment_service.service.PaymentProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PaymentConsumer {

    private final PaymentProcessor paymentProcessor;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PaymentConsumer(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
        System.out.println("PaymentConsumer bean created successfully...");
    }

    @KafkaListener(
            topics = "order-placed-topic",
            groupId = "payment-order-group",
            containerFactory = "orderPlacedKafkaListenerContainerFactory"
    )
    public void consume(String message) {
        try {
            System.out.println("🔥 RAW MESSAGE: " + message);

            OrderPlacedEvent event =
                    objectMapper.readValue(message, OrderPlacedEvent.class);

            System.out.println("🔥 Parsed Event: " + event);

            paymentProcessor.process(event);

        } catch (Exception e) {
            System.out.println("❌ Error parsing message: " + e.getMessage());
        }
    }
}