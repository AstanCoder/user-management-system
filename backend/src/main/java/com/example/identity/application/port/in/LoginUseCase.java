package com.example.identity.application.port.in;

import com.example.identity.application.command.AuthResult;
import com.example.identity.application.command.LoginCommand;

/**
 * Authenticates a user and issues a JWT.
 */
public interface LoginUseCase {

    /**
     * Executes login.
     *
     * @param command login command
     * @return auth result with token
     */
    AuthResult execute(LoginCommand command);
}
