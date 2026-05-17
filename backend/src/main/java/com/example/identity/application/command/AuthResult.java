package com.example.identity.application.command;

import com.example.user.domain.model.Role;
import com.example.user.domain.model.UserId;
import lombok.Builder;
import lombok.Value;

/**
 * Result of successful authentication containing token and user summary.
 */
@Value
@Builder
public class AuthResult {

    String token;
    UserId userId;
    String email;
    String firstName;
    String lastName;
    Role role;
}
