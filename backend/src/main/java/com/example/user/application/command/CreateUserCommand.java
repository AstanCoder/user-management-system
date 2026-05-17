package com.example.user.application.command;

import com.example.user.domain.model.Role;
import lombok.Builder;
import lombok.Value;

/**
 * Input for creating a user via admin API.
 */
@Value
@Builder
public class CreateUserCommand {

    String email;
    String password;
    String firstName;
    String lastName;
    Role role;
}
