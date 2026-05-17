package com.example.user.application.port.in;

import com.example.user.domain.model.UserId;

/**
 * Deletes a user by id.
 */
public interface DeleteUserUseCase {

    /**
     * Deletes the user.
     *
     * @param id user id
     */
    void execute(UserId id);
}
