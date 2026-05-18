package com.example.identity.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.identity.application.command.RegisterCommand;
import com.example.identity.domain.port.PasswordHasher;
import com.example.identity.domain.port.TokenIssuer;
import com.example.identity.domain.port.UserAuthRepository;
import com.example.user.domain.model.Role;
import com.example.user.domain.model.User;
import com.example.user.domain.model.UserId;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RegisterServiceTest {

    private final UserAuthRepository userAuthRepository = Mockito.mock(UserAuthRepository.class);
    private final PasswordHasher passwordHasher = Mockito.mock(PasswordHasher.class);
    private final TokenIssuer tokenIssuer = Mockito.mock(TokenIssuer.class);

    @Test
    void execute_ignoresIncomingRoleAndForcesViewer() {
        RegisterService service = new RegisterService(userAuthRepository, passwordHasher, tokenIssuer);
        RegisterCommand command = RegisterCommand.builder()
                .email("new.user@nexuscrm.com")
                .password("StrongPass123!")
                .firstName("New")
                .lastName("User")
                .role(Role.ADMIN)
                .build();

        when(userAuthRepository.existsByEmail(command.getEmail())).thenReturn(false);
        when(passwordHasher.hash(command.getPassword())).thenReturn("hashed-pass");
        when(userAuthRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(tokenIssuer.issueToken(any())).thenReturn("jwt");

        var result = service.execute(command);

        assertThat(result.getRole()).isEqualTo(Role.VIEWER);
        assertThat(result.getEmail()).isEqualTo("new.user@nexuscrm.com");
        assertThat(result.getUserId()).isInstanceOf(UserId.class);
    }
}
