package com.example.identity.interfaces.rest.dto;

import com.example.user.domain.model.Role;
import lombok.Builder;
import lombok.Value;

/**
 * Authentication response with JWT and user summary.
 */
@Value
@Builder
public class AuthResponse {

    String token;
    String userId;
    String email;
    String firstName;
    String lastName;
    Role role;
}
