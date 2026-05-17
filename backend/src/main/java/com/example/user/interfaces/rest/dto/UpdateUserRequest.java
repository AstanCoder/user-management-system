package com.example.user.interfaces.rest.dto;

import com.example.user.domain.model.Role;
import com.example.user.domain.model.UserStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

/**
 * Update user HTTP request.
 */
@Value
public class UpdateUserRequest {

    @NotBlank String firstName;

    @NotBlank String lastName;

    Role role;

    UserStatus status;
}
