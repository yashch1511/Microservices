package com.ecommerce.order_service.kafka;

import java.util.Optional;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ecommerce.order_service.event.PaymentStatusEvent;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.repository.OrderRepository;

@Service
public class PaymentStatusConsumer {

	private final OrderRepository orderRepository;

	public PaymentStatusConsumer(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
		System.out.println("PaymentStatusConsumer bean created successfully...");
	}

	@KafkaListener(
			topics = "payment-status-topic",
			groupId = "order-payment-group",
			containerFactory = "paymentStatusKafkaListenerContainerFactory"
	)
	public void consume(PaymentStatusEvent event) {
		System.out.println("Received PaymentStatusEvent: " + event);

		if (event.getOrderId() == null) {
			return;
		}

		Optional<Order> maybeOrder = orderRepository.findById(event.getOrderId());
		if (maybeOrder.isEmpty()) {
			System.out.println("Order not found for payment event: orderId=" + event.getOrderId());
			return;
		}

		Order order = maybeOrder.get();
		if ("SUCCESS".equalsIgnoreCase(event.getStatus())) {
			order.setStatus("PAID");
		} else {
			order.setStatus("PAYMENT_FAILED");
		}
		orderRepository.save(order);
	}
}

