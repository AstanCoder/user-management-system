package com.example.user.application.service;

import com.example.user.application.command.CreateUserCommand;
import com.example.user.application.command.UserResult;
import com.example.user.application.mapper.UserApplicationMapper;
import com.example.user.application.port.in.CreateUserUseCase;
import com.example.user.domain.exception.DuplicateUserEmailException;
import com.example.user.domain.model.User;
import com.example.user.domain.model.UserId;
import com.example.user.domain.port.UserRepository;
import com.example.identity.domain.port.PasswordHasher;

/**
 * Creates users with hashed passwords.
 */
public final class CreateUserService implements CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final UserApplicationMapper userApplicationMapper;

    public CreateUserService(
            UserRepository userRepository,
            PasswordHasher passwordHasher,
            UserApplicationMapper userApplicationMapper) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.userApplicationMapper = userApplicationMapper;
    }

    @Override
    public UserResult execute(CreateUserCommand command) {
        userRepository
                .findByEmail(command.getEmail())
                .ifPresent(u -> {
                    throw new DuplicateUserEmailException(command.getEmail());
                });
        String hash = passwordHasher.hash(command.getPassword());
        User user = User.create(
                UserId.generate(),
                command.getEmail(),
                hash,
                command.getFirstName(),
                command.getLastName(),
                command.getRole());
        User saved = userRepository.save(user);
        return userApplicationMapper.toResult(saved);
    }
}
