package com.example.identity.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

/**
 * Reset password request body.
 */
@Value
public class ResetPasswordRequest {

    @NotBlank String token;

    @NotBlank @Size(min = 8) String newPassword;
}
