package com.example.identity.application.service;

import com.example.identity.application.port.in.ResetPasswordUseCase;
import com.example.identity.domain.exception.InvalidPasswordResetTokenException;
import com.example.identity.domain.port.PasswordHasher;
import com.example.identity.domain.port.PasswordResetTokenRepository;
import com.example.identity.domain.port.UserAuthRepository;
import com.example.user.domain.model.UserId;

/**
 * Resets password using a valid token.
 */
public final class ResetPasswordService implements ResetPasswordUseCase {

    private final PasswordResetTokenRepository resetTokenRepository;
    private final UserAuthRepository userAuthRepository;
    private final PasswordHasher passwordHasher;

    public ResetPasswordService(
            PasswordResetTokenRepository resetTokenRepository,
            UserAuthRepository userAuthRepository,
            PasswordHasher passwordHasher) {
        this.resetTokenRepository = resetTokenRepository;
        this.userAuthRepository = userAuthRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public void execute(String token, String newPassword) {
        UserId userId = resetTokenRepository.findValidToken(token).orElseThrow(InvalidPasswordResetTokenException::new);
        String hash = passwordHasher.hash(newPassword);
        userAuthRepository.updatePassword(userId, hash);
        resetTokenRepository.markUsed(token);
    }
}
