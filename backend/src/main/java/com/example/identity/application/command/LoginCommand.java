package com.example.identity.application.command;

import lombok.Builder;
import lombok.Value;

/**
 * Login use case input.
 */
@Value
@Builder
public class LoginCommand {

    String email;
    String password;
}
