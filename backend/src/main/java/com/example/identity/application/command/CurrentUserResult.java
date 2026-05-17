package com.example.identity.application.command;

import com.example.user.domain.model.Role;
import com.example.user.domain.model.UserId;
import com.example.user.domain.model.UserStatus;
import lombok.Builder;
import lombok.Value;

/**
 * Current authenticated user profile.
 */
@Value
@Builder
public class CurrentUserResult {

    UserId id;
    String email;
    String firstName;
    String lastName;
    Role role;
    UserStatus status;
}
