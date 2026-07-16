package com.ubuntuhealth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Define the Password Encoder bean.
     * This uses the BCrypt hashing algorithm to securely store passwords in your MySQL database.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define the Security Filter Chain.
     * This manages which API requests require log-in credentials and which are public.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF (Cross-Site Request Forgery) so Postman/frontend apps can make stateless POST requests
                .csrf(csrf -> csrf.disable())

                // Set up our API endpoint access rules
                .authorizeHttpRequests(auth -> auth
                        // Allow anyone to access the registration and login authentication endpoints
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // Keep all other endpoints locked down until a user is fully authenticated
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}