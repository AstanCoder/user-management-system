package com.example.user.application.service;

import com.example.user.application.port.in.DeleteUserUseCase;
import com.example.user.domain.exception.UserNotFoundException;
import com.example.user.domain.model.UserId;
import com.example.user.domain.port.UserRepository;

/**
 * Deletes a user by id.
 */
public final class DeleteUserService implements DeleteUserUseCase {

    private final UserRepository userRepository;

    public DeleteUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void execute(UserId id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException(id);
        }
        userRepository.delete(id);
    }
}
