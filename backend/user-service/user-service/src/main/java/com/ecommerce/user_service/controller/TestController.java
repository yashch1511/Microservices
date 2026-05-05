package com.ecommerce.user_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/secure")
    public String secure() {
        return "JWT working";
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is a public endpoint";
    }
}