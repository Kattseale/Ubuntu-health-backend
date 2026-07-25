package com.ubuntuhealth.service.impl;

import com.ubuntuhealth.config.JwtUtils;
import com.ubuntuhealth.dto.request.ForgotPasswordRequest;
import com.ubuntuhealth.dto.request.LoginRequest;
import com.ubuntuhealth.dto.request.RegisterRequest;
import com.ubuntuhealth.dto.request.ResetPasswordRequest;
import com.ubuntuhealth.dto.response.AuthResponse;
import com.ubuntuhealth.entity.Role;
import com.ubuntuhealth.entity.RoleName;
import com.ubuntuhealth.entity.User;
import com.ubuntuhealth.exception.BadRequestException;
import com.ubuntuhealth.exception.ResourceNotFoundException;
import com.ubuntuhealth.repository.RoleRepository;
import com.ubuntuhealth.repository.UserRepository;
import com.ubuntuhealth.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered!");
        }

        User user = new User();
        // Common Fields
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setGender(request.getGender());

        // Patient Specific Fields
        user.setIdNumber(request.getIdNumber());
        user.setAddress(request.getAddress());

        // Doctor Specific Fields
        user.setSpecialty(request.getSpecialty());
        user.setLicenseNumber(request.getLicenseNumber());

        // Doctor & Admin Common Fields
        user.setClinicName(request.getClinicName());

        // Admin Specific Fields
        user.setEmployeeNumber(request.getEmployeeNumber());

        // Handle Role Assignment
        Set<Role> roles = new HashSet<>();
        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            Role patientRole = roleRepository.findByName(RoleName.ROLE_PATIENT)
                    .orElseThrow(() -> new ResourceNotFoundException("Role", "name", RoleName.ROLE_PATIENT));
            roles.add(patientRole);
        } else {
            request.getRoles().forEach(roleStr -> {
                RoleName roleName = RoleName.valueOf(
                        roleStr.toUpperCase().startsWith("ROLE_") ? roleStr.toUpperCase() : "ROLE_" + roleStr.toUpperCase()
                );
                Role role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new ResourceNotFoundException("Role", "name", roleStr));
                roles.add(role);
            });
        }
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        // Authenticate user after successful registration
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        List<String> roleList = savedUser.getRoles().stream()
                .map(r -> r.getName().name())
                .toList();

        return AuthResponse.builder()
                .token(jwt)
                .type("Bearer")
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .roles(roleList)
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return AuthResponse.builder()
                .token(jwt)
                .type("Bearer")
                .id(user.getId())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }

    @Override
    public void processForgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        // Optional: Inject an EmailSenderService here to email resetToken to the user
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByResetToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid or expired reset token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }
}