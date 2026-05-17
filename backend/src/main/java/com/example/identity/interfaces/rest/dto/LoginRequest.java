package com.example.identity.interfaces.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

/**
 * Login HTTP request body.
 */
@Value
public class LoginRequest {

    @NotBlank @Email String email;

    @NotBlank String password;
}
