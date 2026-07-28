package com.ubuntuhealth.service;

import com.ubuntuhealth.dto.request.UpdateUserRequest;
import com.ubuntuhealth.dto.response.ApiResponse;
import com.ubuntuhealth.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse getCurrentUser();

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    UserResponse updateUser(Long id, UpdateUserRequest request);

    ApiResponse deleteUser(Long id);

}