package com.ubuntuhealth.service;

import com.ubuntuhealth.dto.request.UserUpdateRequest;
import com.ubuntuhealth.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResponse getCurrentUserProfile();

    Page<UserResponse> getAllUsers(String search, Pageable pageable);

    UserResponse getUserById(Long id);

    UserResponse updateUser(Long id, UserUpdateRequest request);

    void deleteUser(Long id);
}