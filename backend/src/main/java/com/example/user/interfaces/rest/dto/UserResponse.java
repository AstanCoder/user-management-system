package com.example.user.interfaces.rest.dto;

import com.example.user.domain.model.Role;
import com.example.user.domain.model.UserStatus;
import java.time.Instant;
import lombok.Builder;
import lombok.Value;

/**
 * User HTTP response DTO.
 */
@Value
@Builder
public class UserResponse {

    String id;
    String email;
    String firstName;
    String lastName;
    Role role;
    UserStatus status;
    Instant createdAt;
    Instant updatedAt;
    Instant lastActiveAt;
}
