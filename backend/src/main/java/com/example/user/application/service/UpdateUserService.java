package com.example.user.application.service;

import com.example.user.application.command.UpdateUserCommand;
import com.example.user.application.command.UserResult;
import com.example.user.application.mapper.UserApplicationMapper;
import com.example.user.application.port.in.UpdateUserUseCase;
import com.example.user.domain.exception.UserNotFoundException;
import com.example.user.domain.model.User;
import com.example.user.domain.port.UserRepository;

/**
 * Updates user profile and role.
 */
public final class UpdateUserService implements UpdateUserUseCase {

    private final UserRepository userRepository;
    private final UserApplicationMapper userApplicationMapper;

    public UpdateUserService(UserRepository userRepository, UserApplicationMapper userApplicationMapper) {
        this.userRepository = userRepository;
        this.userApplicationMapper = userApplicationMapper;
    }

    @Override
    public UserResult execute(UpdateUserCommand command) {
        User user = userRepository
                .findById(command.getId())
                .orElseThrow(() -> new UserNotFoundException(command.getId()));
        user.update(command.getFirstName(), command.getLastName(), command.getRole(), command.getStatus());
        User saved = userRepository.save(user);
        return userApplicationMapper.toResult(saved);
    }
}
