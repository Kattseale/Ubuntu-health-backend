package com.ubuntuhealth.service;

import com.ubuntuhealth.dto.AuthResponse;
import com.ubuntuhealth.dto.LoginRequest;
import com.ubuntuhealth.model.User;
import com.ubuntuhealth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Single constructor for automatic Spring Dependency Injection
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Registers a new user (Patient, Doctor, or Admin) in the system.
     * Hashes the password using BCrypt before storing it in MySQL.
     */
    public User registerUser(User user) {
        // 1. Check if the username (ID / Employee Num) is already taken
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username (ID/Employee Number) is already registered!");
        }

        // 2. Check if the email is already taken
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email is already registered!");
        }

        // 3. Hash the raw password before saving to database
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 4. Save the user to the MySQL database
        return userRepository.save(user);
    }

    /**
     * Validates user credentials during login.
     * Returns a JWT access token if authentication succeeds.
     */
    public AuthResponse login(LoginRequest request) {
        // 1. Find user by their username (ID or Employee Number)
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        // 2. Safely compare the incoming raw password with the hashed password in MySQL
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // 3. Generate the JWT token populated with the username and role
        String token = jwtService.generateToken(user.getUsername(), user.getRole());

        // 4. Return the complete login response
        return new AuthResponse(token, user.getUsername(), user.getRole());
    }
}