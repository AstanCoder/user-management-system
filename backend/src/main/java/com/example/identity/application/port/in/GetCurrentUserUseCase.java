package com.example.identity.application.port.in;

import com.example.identity.application.command.CurrentUserResult;
import com.example.user.domain.model.UserId;

/**
 * Loads the current authenticated user profile.
 */
public interface GetCurrentUserUseCase {

    /**
     * Returns current user by id.
     *
     * @param userId authenticated user id
     * @return current user result
     */
    CurrentUserResult execute(UserId userId);
}
