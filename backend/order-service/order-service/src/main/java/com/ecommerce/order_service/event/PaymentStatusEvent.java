package com.ecommerce.order_service.event;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentStatusEvent {
	private Long orderId;
	private Long paymentId;
	private String status; // SUCCESS | FAILED
	private Double amount;
	private String message;
	private Instant processedAt;
}

