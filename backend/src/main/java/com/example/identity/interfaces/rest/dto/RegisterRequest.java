package com.example.identity.interfaces.rest.dto;

import com.example.user.domain.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

/**
 * Registration HTTP request body.
 */
@Value
public class RegisterRequest {

    @NotBlank @Email String email;

    @NotBlank @Size(min = 8) String password;

    @NotBlank String firstName;

    @NotBlank String lastName;

    Role role;
}
