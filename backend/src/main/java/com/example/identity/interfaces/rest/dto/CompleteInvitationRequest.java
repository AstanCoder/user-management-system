package com.example.identity.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

/**
 * Complete invitation request body.
 */
@Value
public class CompleteInvitationRequest {

    @NotBlank String token;

    @NotBlank @Size(min = 8) String newPassword;
}
