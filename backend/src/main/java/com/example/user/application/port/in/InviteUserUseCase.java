package com.example.user.application.port.in;

import com.example.user.application.command.InviteUserCommand;
import com.example.user.application.command.UserResult;

/**
 * Invites a user by email without setting a password immediately.
 */
public interface InviteUserUseCase {

    /**
     * Creates invited user and sends invitation email.
     *
     * @param command invite command
     * @return invited user
     */
    UserResult execute(InviteUserCommand command);
}
