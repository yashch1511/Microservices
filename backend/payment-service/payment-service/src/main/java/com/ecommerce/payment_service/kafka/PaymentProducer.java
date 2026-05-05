package com.ecommerce.payment_service.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ecommerce.payment_service.event.PaymentStatusEvent;

@Service
public class PaymentProducer {

	private static final String TOPIC = "payment-status-topic";

	@Autowired
	private KafkaTemplate<String, PaymentStatusEvent> paymentStatusKafkaTemplate;

	public void sendPaymentStatus(PaymentStatusEvent event) {
		paymentStatusKafkaTemplate.send(TOPIC, String.valueOf(event.getOrderId()), event).whenComplete((result, ex) -> {
			if (ex != null) {
				System.out.println("PaymentStatusEvent failed to send: " + ex.getMessage());
			} else {
				System.out.println("PaymentStatusEvent sent: " + event);
			}
		});
	}
}
