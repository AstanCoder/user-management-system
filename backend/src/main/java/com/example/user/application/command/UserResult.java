package com.example.user.application.command;

import com.example.user.domain.model.Role;
import com.example.user.domain.model.UserId;
import com.example.user.domain.model.UserStatus;
import java.time.Instant;
import lombok.Builder;
import lombok.Value;

/**
 * Application-layer representation of a user.
 */
@Value
@Builder
public class UserResult {

    UserId id;
    String email;
    String firstName;
    String lastName;
    Role role;
    UserStatus status;
    Instant createdAt;
    Instant updatedAt;
    Instant lastActiveAt;
}
