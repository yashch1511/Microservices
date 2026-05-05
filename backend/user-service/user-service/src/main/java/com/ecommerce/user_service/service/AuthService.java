package com.ecommerce.user_service.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.user_service.config.JwtUtil;
import com.ecommerce.user_service.dto.LoginResponse;
import com.ecommerce.user_service.model.User;
import com.ecommerce.user_service.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final KafkaProducer kafkaProducer;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       KafkaProducer kafkaProducer) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.kafkaProducer = kafkaProducer;
    }

    public User register(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER");
        } else {
            user.setRole(user.getRole().toUpperCase());
        }

        User savedUser = userRepository.save(user);

        kafkaProducer.sendUserRegisteredEvent(savedUser);

        return savedUser;
    }

    // ✅ NEW
public LoginResponse login(String email, String password) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (!passwordEncoder.matches(password, user.getPassword())) {
        throw new RuntimeException("Invalid password");
    }

    String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

    return new LoginResponse(token, user.getRole()); // ✅ RETURN ROLE ALSO
}
}