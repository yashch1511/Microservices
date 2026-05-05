package com.ecommerce.order_service.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ecommerce.order_service.dto.Product;
import com.ecommerce.order_service.event.OrderPlacedEvent;
import com.ecommerce.order_service.kafka.OrderProducer;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.repository.OrderRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OrderProducer orderProducer;

    public Order placeOrder(Order order, String userEmail, HttpServletRequest request) {
        Product product = restTemplate.getForObject(
                "http://localhost:8083/products/" + order.getProductId(),
                Product.class
        );

        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        if (product.getQuantity() < order.getQuantity()) {
            throw new RuntimeException("Insufficient product quantity");
        }

        order.setUserEmail(userEmail);
        order.setTotalPrice(product.getPrice() * order.getQuantity());

        if (order.getStatus() == null || order.getStatus().isEmpty()) {
            order.setStatus("PLACED");
        }

        int newQuantity = product.getQuantity() - order.getQuantity();

        String authHeader = request.getHeader("Authorization");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange(
                "http://localhost:8083/products/" + order.getProductId() + "/quantity?quantity=" + newQuantity,
                HttpMethod.PUT,
                entity,
                Void.class
        );

        Order savedOrder = orderRepository.save(order);

        OrderPlacedEvent event = new OrderPlacedEvent(
                savedOrder.getId(),
                null,
                savedOrder.getProductId(),
                savedOrder.getQuantity(),
                savedOrder.getTotalPrice(),
                savedOrder.getStatus()
        );

        orderProducer.sendOrderPlacedEvent(event);

        return savedOrder;
    }

    public List<Order> getOrdersByUserEmail(String userEmail) {
        List<Order> orders = orderRepository.findByUserEmail(userEmail);   
        Collections.reverse(orders);
        return orders;
    }

    // CHANGED: safe fetch by id for logged-in user only
    public Order getOrderByIdForUser(Long id, String userEmail) {
        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent() && order.get().getUserEmail().equals(userEmail)) {
            return order.get();
        }

        throw new RuntimeException("Order not found or access denied");
    }

    // optional: you can remove this if you no longer use it
    public Order getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.orElse(null);
    }
}