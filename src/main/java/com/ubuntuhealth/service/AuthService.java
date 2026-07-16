package com.ubuntuhealth.service;

import com.ubuntuhealth.model.User;
import com.ubuntuhealth.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    // Dependency Injection via Constructor
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        // 1. Check if the username (ID / Employee Num) is already taken[cite: 1]
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username (ID/Employee Number) is already registered!");
        }

        // 2. Check if the email is already taken
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email is already registered!");
        }

        // Note: We will add BCrypt password encoding here once Spring Security is active.[cite: 1]

        // 3. Save the user to the MySQL database
        return userRepository.save(user);
    }
}