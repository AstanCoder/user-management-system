package com.example.identity.infrastructure.config;

import com.example.identity.domain.port.InvitationTokenRepository;
import com.example.identity.domain.port.PasswordResetTokenRepository;
import java.time.Instant;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AuthTokenCleanupScheduler {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final InvitationTokenRepository invitationTokenRepository;

    public AuthTokenCleanupScheduler(
            PasswordResetTokenRepository passwordResetTokenRepository,
            InvitationTokenRepository invitationTokenRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.invitationTokenRepository = invitationTokenRepository;
    }

    @Scheduled(cron = "${app.identity.token-cleanup-cron:0 */30 * * * *}")
    public void cleanupExpiredTokens() {
        Instant now = Instant.now();
        passwordResetTokenRepository.deleteExpiredUnused(now);
        invitationTokenRepository.deleteExpiredUnused(now);
    }
}
