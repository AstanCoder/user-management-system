package com.example.user.infrastructure.config;

import com.example.identity.domain.port.PasswordHasher;
import com.example.user.application.mapper.UserApplicationMapper;
import com.example.user.application.port.in.CreateUserUseCase;
import com.example.user.application.port.in.DeleteUserUseCase;
import com.example.user.application.port.in.GetUserStatsUseCase;
import com.example.user.application.port.in.InviteUserUseCase;
import com.example.user.application.port.in.ListUsersUseCase;
import com.example.user.application.port.in.UpdateUserUseCase;
import com.example.user.application.service.CreateUserService;
import com.example.user.application.service.DeleteUserService;
import com.example.user.application.service.GetUserStatsService;
import com.example.user.application.service.InviteUserService;
import com.example.user.application.service.ListUsersService;
import com.example.user.application.service.UpdateUserService;
import com.example.user.domain.port.UserInvitationEmailSender;
import com.example.user.domain.port.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Composition root for user bounded context use cases.
 */
@Configuration
public class UserModuleConfig {

    @Bean
    public ListUsersUseCase listUsersUseCase(
            UserRepository userRepository, UserApplicationMapper userApplicationMapper) {
        return new ListUsersService(userRepository, userApplicationMapper);
    }

    @Bean
    public GetUserStatsUseCase getUserStatsUseCase(UserRepository userRepository) {
        return new GetUserStatsService(userRepository);
    }

    @Bean
    public CreateUserUseCase createUserUseCase(
            UserRepository userRepository,
            PasswordHasher passwordHasher,
            UserApplicationMapper userApplicationMapper) {
        return new CreateUserService(userRepository, passwordHasher, userApplicationMapper);
    }

    @Bean
    public UpdateUserUseCase updateUserUseCase(
            UserRepository userRepository, UserApplicationMapper userApplicationMapper) {
        return new UpdateUserService(userRepository, userApplicationMapper);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase(UserRepository userRepository) {
        return new DeleteUserService(userRepository);
    }

    @Bean
    public InviteUserUseCase inviteUserUseCase(
            UserRepository userRepository,
            UserInvitationEmailSender invitationEmailSender,
            UserApplicationMapper userApplicationMapper) {
        return new InviteUserService(userRepository, invitationEmailSender, userApplicationMapper);
    }
}
