package com.ubuntuhealth.dto.response;

import com.ubuntuhealth.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private String token;

    private String email;

    private Role role;

    private String message;

}