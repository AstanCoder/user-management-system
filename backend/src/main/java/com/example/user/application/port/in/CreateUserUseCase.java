package com.example.user.application.port.in;

import com.example.user.application.command.CreateUserCommand;
import com.example.user.application.command.UserResult;

/**
 * Creates a new user with password.
 */
public interface CreateUserUseCase {

    /**
     * Creates a user.
     *
     * @param command create command
     * @return created user
     */
    UserResult execute(CreateUserCommand command);
}
