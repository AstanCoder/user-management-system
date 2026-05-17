package com.example.user.interfaces.rest.dto;

import com.example.user.domain.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

/**
 * Create user HTTP request.
 */
@Value
public class CreateUserRequest {

    @NotBlank @Email String email;

    @NotBlank @Size(min = 8) String password;

    @NotBlank String firstName;

    @NotBlank String lastName;

    Role role;
}
