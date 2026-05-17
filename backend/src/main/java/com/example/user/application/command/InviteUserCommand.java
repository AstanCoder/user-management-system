package com.example.user.application.command;

import com.example.user.domain.model.Role;
import lombok.Builder;
import lombok.Value;

/**
 * Input for inviting a new user.
 */
@Value
@Builder
public class InviteUserCommand {

    String email;
    String firstName;
    String lastName;
    Role role;
}
