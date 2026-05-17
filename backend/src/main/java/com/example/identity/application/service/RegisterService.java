package com.example.identity.application.service;

import com.example.identity.application.command.AuthResult;
import com.example.identity.application.command.RegisterCommand;
import com.example.identity.application.port.in.RegisterUseCase;
import com.example.identity.domain.exception.UserAlreadyExistsException;
import com.example.identity.domain.model.AuthUser;
import com.example.identity.domain.port.PasswordHasher;
import com.example.identity.domain.port.TokenIssuer;
import com.example.identity.domain.port.UserAuthRepository;
import com.example.user.domain.model.Role;
import com.example.user.domain.model.User;
import com.example.user.domain.model.UserId;

/**
 * Registers a new user and returns JWT.
 */
public final class RegisterService implements RegisterUseCase {

    private final UserAuthRepository userAuthRepository;
    private final PasswordHasher passwordHasher;
    private final TokenIssuer tokenIssuer;

    public RegisterService(
            UserAuthRepository userAuthRepository, PasswordHasher passwordHasher, TokenIssuer tokenIssuer) {
        this.userAuthRepository = userAuthRepository;
        this.passwordHasher = passwordHasher;
        this.tokenIssuer = tokenIssuer;
    }

    @Override
    public AuthResult execute(RegisterCommand command) {
        if (userAuthRepository.existsByEmail(command.getEmail())) {
            throw new UserAlreadyExistsException(command.getEmail());
        }
        Role role = command.getRole() != null ? command.getRole() : Role.VIEWER;
        String hash = passwordHasher.hash(command.getPassword());
        User user = User.create(
                UserId.generate(),
                command.getEmail(),
                hash,
                command.getFirstName(),
                command.getLastName(),
                role);
        User saved = userAuthRepository.save(user);
        AuthUser authUser = new AuthUser(
                saved.id(),
                saved.email(),
                saved.passwordHash(),
                saved.role(),
                saved.status(),
                saved.firstName(),
                saved.lastName());
        String token = tokenIssuer.issueToken(authUser);
        return AuthResult.builder()
                .token(token)
                .userId(saved.id())
                .email(saved.email())
                .firstName(saved.firstName())
                .lastName(saved.lastName())
                .role(saved.role())
                .build();
    }
}
