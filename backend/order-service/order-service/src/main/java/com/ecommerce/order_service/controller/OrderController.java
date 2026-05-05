package com.ecommerce.order_service.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Order placeOrder(@RequestBody Order order, Principal principal, HttpServletRequest request) {
        return orderService.placeOrder(order, principal.getName(), request);
    }

    @GetMapping
    public List<Order> getMyOrders(Principal principal) {
        return orderService.getOrdersByUserEmail(principal.getName());
    }

    // CHANGED: now only returns order if it belongs to logged-in user
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id, Principal principal) {
        return orderService.getOrderByIdForUser(id, principal.getName());
    }
}