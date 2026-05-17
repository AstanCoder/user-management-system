package com.example.identity.application.port.in;

import com.example.identity.application.command.AuthResult;
import com.example.identity.application.command.RegisterCommand;

/**
 * Registers a new user account.
 */
public interface RegisterUseCase {

    /**
     * Registers user and returns auth token.
     *
     * @param command register command
     * @return auth result
     */
    AuthResult execute(RegisterCommand command);
}
