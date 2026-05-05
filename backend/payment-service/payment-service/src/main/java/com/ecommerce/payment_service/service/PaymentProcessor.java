package com.ecommerce.payment_service.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.payment_service.event.OrderPlacedEvent;
import com.ecommerce.payment_service.event.PaymentStatusEvent;
import com.ecommerce.payment_service.kafka.PaymentProducer;
import com.ecommerce.payment_service.model.Payment;
import com.ecommerce.payment_service.repository.PaymentRepository;

@Service
public class PaymentProcessor {

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private PaymentProducer paymentProducer;

	public PaymentStatusEvent process(OrderPlacedEvent orderPlacedEvent) {
		boolean success = isSuccessful(orderPlacedEvent.getOrderId());
		String status = success ? "SUCCESS" : "FAILED";
		String message = success ? "Payment captured (mock)" : "Payment declined (mock)";
		Instant processedAt = Instant.now();

		Payment payment = new Payment();
		payment.setOrderId(orderPlacedEvent.getOrderId());
		payment.setAmount(orderPlacedEvent.getTotalPrice());
		payment.setStatus(status);
		payment.setMessage(message);
		payment.setProcessedAt(processedAt);

		Payment saved = paymentRepository.save(payment);

		PaymentStatusEvent event = new PaymentStatusEvent(
				orderPlacedEvent.getOrderId(),
				saved.getId(),
				status,
				orderPlacedEvent.getTotalPrice(),
				message,
				processedAt
		);

		paymentProducer.sendPaymentStatus(event);
		return event;
	}

	// Deterministic mock: every 5th order fails.
	private boolean isSuccessful(Long orderId) {
		if (orderId == null) {
			return false;
		}
		return orderId % 5 != 0;
	}
}

