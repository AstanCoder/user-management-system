package com.example.identity.application.service;

import com.example.identity.application.command.AuthResult;
import com.example.identity.application.command.CompleteInvitationCommand;
import com.example.identity.application.port.in.CompleteInvitationUseCase;
import com.example.identity.domain.exception.AuthUserNotFoundException;
import com.example.identity.domain.exception.InvalidInvitationTokenException;
import com.example.identity.domain.model.AuthUser;
import com.example.identity.domain.port.InvitationTokenRepository;
import com.example.identity.domain.port.PasswordHasher;
import com.example.identity.domain.port.TokenIssuer;
import com.example.identity.domain.port.UserAuthRepository;
import com.example.user.domain.model.UserStatus;

/**
 * Completes invited user registration and issues JWT.
 */
public final class CompleteInvitationService implements CompleteInvitationUseCase {

    private final InvitationTokenRepository invitationTokenRepository;
    private final UserAuthRepository userAuthRepository;
    private final PasswordHasher passwordHasher;
    private final TokenIssuer tokenIssuer;

    public CompleteInvitationService(
            InvitationTokenRepository invitationTokenRepository,
            UserAuthRepository userAuthRepository,
            PasswordHasher passwordHasher,
            TokenIssuer tokenIssuer) {
        this.invitationTokenRepository = invitationTokenRepository;
        this.userAuthRepository = userAuthRepository;
        this.passwordHasher = passwordHasher;
        this.tokenIssuer = tokenIssuer;
    }

    @Override
    public AuthResult execute(CompleteInvitationCommand command) {
        var userId = invitationTokenRepository
                .findValidToken(command.getToken())
                .orElseThrow(InvalidInvitationTokenException::new);
        AuthUser authUser = userAuthRepository
                .findById(userId)
                .orElseThrow(() -> new AuthUserNotFoundException(userId));
        if (authUser.status() != UserStatus.INVITED) {
            throw new InvalidInvitationTokenException();
        }
        String hash = passwordHasher.hash(command.getNewPassword());
        userAuthRepository.updatePassword(userId, hash);
        invitationTokenRepository.markUsed(command.getToken());
        AuthUser updatedUser = userAuthRepository
                .findById(userId)
                .orElseThrow(() -> new AuthUserNotFoundException(userId));
        String token = tokenIssuer.issueToken(updatedUser);
        return AuthResult.builder()
                .token(token)
                .userId(updatedUser.id())
                .email(updatedUser.email())
                .firstName(updatedUser.firstName())
                .lastName(updatedUser.lastName())
                .role(updatedUser.role())
                .build();
    }
}
