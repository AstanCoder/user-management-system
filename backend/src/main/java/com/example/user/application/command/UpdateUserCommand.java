package com.example.user.application.command;

import com.example.user.domain.model.Role;
import com.example.user.domain.model.UserId;
import com.example.user.domain.model.UserStatus;
import lombok.Builder;
import lombok.Value;

/**
 * Input for updating a user.
 */
@Value
@Builder
public class UpdateUserCommand {

    UserId id;
    String firstName;
    String lastName;
    Role role;
    UserStatus status;
}
