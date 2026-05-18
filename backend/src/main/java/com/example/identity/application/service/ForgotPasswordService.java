package com.example.identity.application.service;

import com.example.identity.application.port.in.ForgotPasswordUseCase;
import com.example.identity.domain.model.AuthUser;
import com.example.identity.domain.port.EmailSender;
import com.example.identity.domain.port.PasswordResetTokenRepository;
import com.example.identity.domain.port.UserAuthRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Sends password reset email when user exists.
 */
public final class ForgotPasswordService implements ForgotPasswordUseCase {

    private final UserAuthRepository userAuthRepository;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final EmailSender emailSender;
    private final String mailFrom;
    private final long resetTokenExpirationMinutes;

    public ForgotPasswordService(
            UserAuthRepository userAuthRepository,
            PasswordResetTokenRepository resetTokenRepository,
            EmailSender emailSender,
            String mailFrom,
            long resetTokenExpirationMinutes) {
        this.userAuthRepository = userAuthRepository;
        this.resetTokenRepository = resetTokenRepository;
        this.emailSender = emailSender;
        this.mailFrom = mailFrom;
        this.resetTokenExpirationMinutes = resetTokenExpirationMinutes;
    }

    @Override
    public void execute(String email) {
        userAuthRepository.findByEmail(email).ifPresent(this::sendResetEmail);
    }

    private void sendResetEmail(AuthUser user) {
        String token = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plus(resetTokenExpirationMinutes, ChronoUnit.MINUTES);
        resetTokenRepository.save(user.id(), token, expiresAt);
        String subject = "Nexus CRM password reset";
        String body = "Hello,\n\nUse this token to reset your password: " + token + "\n\n— Nexus CRM";
        emailSender.send(mailFrom, user.email(), subject, body);
    }
}
