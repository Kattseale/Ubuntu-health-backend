package com.ubuntuhealth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                // =====================================================
                // CSRF
                // =====================================================

                .csrf(csrf -> csrf.disable())


                // =====================================================
                // CORS
                // =====================================================

                .cors(Customizer.withDefaults())


                // =====================================================
                // SESSION
                // =====================================================

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )


                // =====================================================
                // AUTHORIZATION
                // =====================================================

                .authorizeHttpRequests(auth -> auth

                        // =================================================
                        // PUBLIC
                        // =================================================

                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        )
                        .permitAll()


                        // =================================================
                        // CLINICS
                        // =================================================

                        .requestMatchers("/api/clinics/**")
                        .hasAnyRole(
                                "PATIENT",
                                "ADMIN"
                        )


                                // =====================================================
// APPOINTMENTS
// =====================================================

// -----------------------------------------------------
// CREATE APPOINTMENT
// PATIENT + ADMIN
// -----------------------------------------------------

                                .requestMatchers(
                                        org.springframework.http.HttpMethod.POST,
                                        "/api/appointments"
                                )
                                .hasAnyRole(
                                        "PATIENT",
                                        "ADMIN"
                                )


// -----------------------------------------------------
// MY APPOINTMENTS
// PATIENT ONLY
// -----------------------------------------------------

                                .requestMatchers(
                                        org.springframework.http.HttpMethod.GET,
                                        "/api/appointments/my"
                                )
                                .hasRole("PATIENT")


// -----------------------------------------------------
// ALL APPOINTMENTS
// ADMIN ONLY
// -----------------------------------------------------

                                .requestMatchers(
                                        org.springframework.http.HttpMethod.GET,
                                        "/api/appointments"
                                )
                                .hasRole("ADMIN")


// -----------------------------------------------------
// CLINIC APPOINTMENTS
// PATIENT + ADMIN
// -----------------------------------------------------

                                .requestMatchers(
                                        org.springframework.http.HttpMethod.GET,
                                        "/api/appointments/clinic/**"
                                )
                                .hasAnyRole(
                                        "PATIENT",
                                        "ADMIN"
                                )


// -----------------------------------------------------
// APPOINTMENTS BY DATE
// PATIENT + ADMIN
// -----------------------------------------------------

                                .requestMatchers(
                                        org.springframework.http.HttpMethod.GET,
                                        "/api/appointments/date/**"
                                )
                                .hasAnyRole(
                                        "PATIENT",
                                        "ADMIN"
                                )


// -----------------------------------------------------
// APPOINTMENTS BY PATIENT
// ADMIN ONLY
// -----------------------------------------------------

                                .requestMatchers(
                                        org.springframework.http.HttpMethod.GET,
                                        "/api/appointments/patient/**"
                                )
                                .hasRole("ADMIN")


// -----------------------------------------------------
// APPOINTMENT BY ID
// ADMIN ONLY
// -----------------------------------------------------

                                .requestMatchers(
                                        org.springframework.http.HttpMethod.GET,
                                        "/api/appointments/*"
                                )
                                .hasRole("ADMIN")


// -----------------------------------------------------
// UPDATE
// ADMIN ONLY
// -----------------------------------------------------

                                .requestMatchers(
                                        org.springframework.http.HttpMethod.PUT,
                                        "/api/appointments/*"
                                )
                                .hasRole("ADMIN")


// -----------------------------------------------------
// CANCEL
// PATIENT + ADMIN
// -----------------------------------------------------

                                .requestMatchers(
                                        org.springframework.http.HttpMethod.DELETE,
                                        "/api/appointments/*"
                                )
                                .hasAnyRole(
                                        "PATIENT",
                                        "ADMIN"
                                )


// -----------------------------------------------------
// COMPLETE
// ADMIN ONLY
// -----------------------------------------------------

                                .requestMatchers(
                                        org.springframework.http.HttpMethod.PUT,
                                        "/api/appointments/*/complete"
                                )
                                .hasRole("ADMIN")

                        // =================================================
                        // ANNOUNCEMENTS
                        // =================================================

                        .requestMatchers(
                                "/api/announcements/**"
                        )
                        .authenticated()


                        // =================================================
                        // ADMIN
                        // =================================================

                        .requestMatchers(
                                "/api/admin/**"
                        )
                        .hasRole("ADMIN")


                        // =================================================
                        // DOCTOR
                        // =================================================

                        .requestMatchers(
                                "/api/doctor/**"
                        )
                        .hasRole("DOCTOR")


                        // =================================================
                        // RECEPTIONIST
                        // =================================================

                        .requestMatchers(
                                "/api/receptionist/**"
                        )
                        .hasRole("RECEPTIONIST")


                        // =================================================
                        // PATIENT
                        // =================================================

                        .requestMatchers(
                                "/api/patient/**"
                        )
                        .hasRole("PATIENT")


                        // =================================================
                        // EVERYTHING ELSE
                        // =================================================

                        .anyRequest()
                        .authenticated()
                )


                // =====================================================
                // AUTHENTICATION PROVIDER
                // =====================================================

                .authenticationProvider(
                        authenticationProvider()
                )


                // =====================================================
                // JWT FILTER
                // =====================================================

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );


        return http.build();
    }


    // =============================================================
    // AUTHENTICATION PROVIDER
    // =============================================================

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();

        provider.setUserDetailsService(
                customUserDetailsService
        );

        provider.setPasswordEncoder(
                passwordEncoder
        );

        return provider;
    }


    // =============================================================
    // AUTHENTICATION MANAGER
    // =============================================================

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {

        return configuration.getAuthenticationManager();
    }
}