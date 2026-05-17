package com.example.identity.infrastructure.config;

import com.example.identity.application.port.in.ForgotPasswordUseCase;
import com.example.identity.application.port.in.GetCurrentUserUseCase;
import com.example.identity.application.port.in.LoginUseCase;
import com.example.identity.application.port.in.RegisterUseCase;
import com.example.identity.application.port.in.ResetPasswordUseCase;
import com.example.identity.application.service.ForgotPasswordService;
import com.example.identity.application.service.GetCurrentUserService;
import com.example.identity.application.service.LoginService;
import com.example.identity.application.service.RegisterService;
import com.example.identity.application.service.ResetPasswordService;
import com.example.identity.domain.port.EmailSender;
import com.example.identity.domain.port.PasswordHasher;
import com.example.identity.domain.port.PasswordResetTokenRepository;
import com.example.identity.domain.port.TokenIssuer;
import com.example.identity.domain.port.UserAuthRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Composition root for identity bounded context use cases.
 */
@Configuration
public class IdentityModuleConfig {

    @Bean
    public LoginUseCase loginUseCase(
            UserAuthRepository userAuthRepository, PasswordHasher passwordHasher, TokenIssuer tokenIssuer) {
        return new LoginService(userAuthRepository, passwordHasher, tokenIssuer);
    }

    @Bean
    public RegisterUseCase registerUseCase(
            UserAuthRepository userAuthRepository, PasswordHasher passwordHasher, TokenIssuer tokenIssuer) {
        return new RegisterService(userAuthRepository, passwordHasher, tokenIssuer);
    }

    @Bean
    public GetCurrentUserUseCase getCurrentUserUseCase(UserAuthRepository userAuthRepository) {
        return new GetCurrentUserService(userAuthRepository);
    }

    @Bean
    public ForgotPasswordUseCase forgotPasswordUseCase(
            UserAuthRepository userAuthRepository,
            PasswordResetTokenRepository resetTokenRepository,
            EmailSender emailSender,
            @Value("${app.mail.from:noreply@nexuscrm.com}") String mailFrom) {
        return new ForgotPasswordService(userAuthRepository, resetTokenRepository, emailSender, mailFrom);
    }

    @Bean
    public ResetPasswordUseCase resetPasswordUseCase(
            PasswordResetTokenRepository resetTokenRepository,
            UserAuthRepository userAuthRepository,
            PasswordHasher passwordHasher) {
        return new ResetPasswordService(resetTokenRepository, userAuthRepository, passwordHasher);
    }
}
