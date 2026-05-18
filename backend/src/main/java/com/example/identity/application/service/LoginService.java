package com.example.identity.application.service;

import com.example.identity.application.command.AuthResult;
import com.example.identity.application.command.LoginCommand;
import com.example.identity.application.port.in.LoginUseCase;
import com.example.identity.domain.exception.InvalidCredentialsException;
import com.example.identity.domain.model.AuthUser;
import com.example.identity.domain.port.PasswordHasher;
import com.example.identity.domain.port.TokenIssuer;
import com.example.identity.domain.port.UserAuthRepository;

/**
 * Authenticates credentials and issues JWT.
 */
public final class LoginService implements LoginUseCase {

    private final UserAuthRepository userAuthRepository;
    private final PasswordHasher passwordHasher;
    private final TokenIssuer tokenIssuer;

    public LoginService(
            UserAuthRepository userAuthRepository, PasswordHasher passwordHasher, TokenIssuer tokenIssuer) {
        this.userAuthRepository = userAuthRepository;
        this.passwordHasher = passwordHasher;
        this.tokenIssuer = tokenIssuer;
    }

    @Override
    public AuthResult execute(LoginCommand command) {
        AuthUser user = userAuthRepository
                .findByEmail(command.getEmail())
                .orElseThrow(InvalidCredentialsException::new);
        if (!user.isActive() || user.passwordHash() == null) {
            throw new InvalidCredentialsException();
        }
        if (!passwordHasher.matches(command.getPassword(), user.passwordHash())) {
            throw new InvalidCredentialsException();
        }
        userAuthRepository.recordLastActiveAt(user.id());
        String token = tokenIssuer.issueToken(user);
        return AuthResult.builder()
                .token(token)
                .userId(user.id())
                .email(user.email())
                .firstName(user.firstName())
                .lastName(user.lastName())
                .role(user.role())
                .build();
    }
}
