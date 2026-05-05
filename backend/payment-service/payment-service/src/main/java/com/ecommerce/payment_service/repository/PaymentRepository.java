package com.ecommerce.payment_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.payment_service.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	Optional<Payment> findTopByOrderIdOrderByIdDesc(Long orderId);
}

