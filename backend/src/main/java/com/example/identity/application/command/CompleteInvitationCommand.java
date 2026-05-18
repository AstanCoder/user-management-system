package com.example.identity.application.command;

import lombok.Builder;
import lombok.Value;

/**
 * Complete invitation input with token and chosen password.
 */
@Value
@Builder
public class CompleteInvitationCommand {

    String token;
    String newPassword;
}
