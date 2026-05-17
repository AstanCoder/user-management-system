package com.example.identity.interfaces.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

/**
 * Forgot password request body.
 */
@Value
public class ForgotPasswordRequest {

    @NotBlank @Email String email;
}
