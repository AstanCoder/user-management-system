package com.example.identity.application.command;

import com.example.user.domain.model.Role;
import lombok.Builder;
import lombok.Value;

/**
 * Self-registration use case input.
 */
@Value
@Builder
public class RegisterCommand {

    String email;
    String password;
    String firstName;
    String lastName;
    Role role;
}
