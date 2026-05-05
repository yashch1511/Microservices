package com.ecommerce.payment_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.payment_service.model.Payment;
import com.ecommerce.payment_service.repository.PaymentRepository;

@RestController
@RequestMapping("/payments")
public class PaymentController {

	private final PaymentRepository paymentRepository;

	public PaymentController(PaymentRepository paymentRepository) {
		this.paymentRepository = paymentRepository;
	}

	@GetMapping
	public List<Payment> list() {
		return paymentRepository.findAll();
	}

	@GetMapping("/order/{orderId}")
	public ResponseEntity<Payment> latestByOrderId(@PathVariable Long orderId) {
		return paymentRepository.findTopByOrderIdOrderByIdDesc(orderId)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
}

