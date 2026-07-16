package com.ubuntuhealth.repository;


import com.ubuntuhealth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // This allows us to look up users by their login credential (ID / Employee Num)[cite: 1]
    Optional<User> findByUsername(String username);

    // This will help check if an ID or Email is already registered[cite: 1]
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}