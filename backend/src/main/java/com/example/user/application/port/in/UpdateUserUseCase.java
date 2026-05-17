package com.example.user.application.port.in;

import com.example.user.application.command.UpdateUserCommand;
import com.example.user.application.command.UserResult;

/**
 * Updates an existing user.
 */
public interface UpdateUserUseCase {

    /**
     * Updates a user.
     *
     * @param command update command
     * @return updated user
     */
    UserResult execute(UpdateUserCommand command);
}
