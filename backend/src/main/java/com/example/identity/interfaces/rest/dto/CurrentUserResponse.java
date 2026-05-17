package com.example.identity.interfaces.rest.dto;

import com.example.user.domain.model.Role;
import com.example.user.domain.model.UserStatus;
import lombok.Builder;
import lombok.Value;

/**
 * Current user profile HTTP response.
 */
@Value
@Builder
public class CurrentUserResponse {

    String id;
    String email;
    String firstName;
    String lastName;
    Role role;
    UserStatus status;
}
