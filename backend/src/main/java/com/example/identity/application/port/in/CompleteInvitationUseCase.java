package com.example.identity.application.port.in;

import com.example.identity.application.command.AuthResult;
import com.example.identity.application.command.CompleteInvitationCommand;

/**
 * Completes invited user registration and returns JWT session.
 */
public interface CompleteInvitationUseCase {

    /**
     * Completes invitation and authenticates the user.
     *
     * @param command completion payload
     * @return auth result with JWT
     */
    AuthResult execute(CompleteInvitationCommand command);
}
