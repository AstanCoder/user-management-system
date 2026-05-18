package com.example.identity.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.identity.domain.model.AuthUser;
import com.example.identity.domain.port.EmailSender;
import com.example.identity.domain.port.PasswordResetTokenRepository;
import com.example.identity.domain.port.UserAuthRepository;
import com.example.user.domain.model.Role;
import com.example.user.domain.model.UserId;
import com.example.user.domain.model.UserStatus;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class ForgotPasswordServiceTest {

    private final UserAuthRepository userAuthRepository = Mockito.mock(UserAuthRepository.class);
    private final PasswordResetTokenRepository resetTokenRepository = Mockito.mock(PasswordResetTokenRepository.class);
    private final EmailSender emailSender = Mockito.mock(EmailSender.class);

    @Test
    void execute_savesTokenWithConfiguredExpirationWindow() {
        long expirationMinutes = 30L;
        ForgotPasswordService service = new ForgotPasswordService(
                userAuthRepository, resetTokenRepository, emailSender, "noreply@nexuscrm.com", expirationMinutes);
        UserId userId = UserId.generate();
        AuthUser user = new AuthUser(userId, "user@nexuscrm.com", "hash", Role.VIEWER, UserStatus.ACTIVE, "U", "S");

        when(userAuthRepository.findByEmail("user@nexuscrm.com")).thenReturn(Optional.of(user));
        ArgumentCaptor<Instant> expiresCaptor = ArgumentCaptor.forClass(Instant.class);
        Instant before = Instant.now();

        service.execute("user@nexuscrm.com");

        verify(resetTokenRepository).save(eq(userId), any(String.class), expiresCaptor.capture());
        Instant expiresAt = expiresCaptor.getValue();
        Instant expectedLowerBound = before.plus(expirationMinutes - 1, ChronoUnit.MINUTES);
        Instant expectedUpperBound = before.plus(expirationMinutes + 1, ChronoUnit.MINUTES);
        assertThat(expiresAt).isAfter(expectedLowerBound);
        assertThat(expiresAt).isBefore(expectedUpperBound);
        verify(emailSender).send(eq("noreply@nexuscrm.com"), eq("user@nexuscrm.com"), any(String.class), any(String.class));
    }

    @Test
    void execute_nonExistingEmailDoesNothing() {
        ForgotPasswordService service =
                new ForgotPasswordService(userAuthRepository, resetTokenRepository, emailSender, "noreply@nexuscrm.com", 30L);
        when(userAuthRepository.findByEmail("unknown@nexuscrm.com")).thenReturn(Optional.empty());

        service.execute("unknown@nexuscrm.com");

        verify(resetTokenRepository, never()).save(any(), any(), any());
        verify(emailSender, never()).send(any(), any(), any(), any());
    }
}
