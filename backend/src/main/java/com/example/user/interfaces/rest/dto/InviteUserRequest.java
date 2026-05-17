package com.example.user.interfaces.rest.dto;

import com.example.user.domain.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

/**
 * Invite user HTTP request.
 */
@Value
public class InviteUserRequest {

    @NotBlank @Email String email;

    @NotBlank String firstName;

    @NotBlank String lastName;

    Role role;
}
