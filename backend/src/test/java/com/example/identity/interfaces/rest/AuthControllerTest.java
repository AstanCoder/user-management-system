package com.example.identity.interfaces.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.identity.application.command.AuthResult;
import com.example.identity.application.command.CurrentUserResult;
import com.example.identity.application.port.in.ForgotPasswordUseCase;
import com.example.identity.application.port.in.GetCurrentUserUseCase;
import com.example.identity.application.port.in.LoginUseCase;
import com.example.identity.application.port.in.RegisterUseCase;
import com.example.identity.application.port.in.ResetPasswordUseCase;
import com.example.identity.config.AuthTestSecurityConfig;
import com.example.identity.domain.exception.InvalidCredentialsException;
import com.example.identity.infrastructure.config.SecurityConfig;
import com.example.identity.infrastructure.security.AuthenticatedUserPrincipal;
import com.example.identity.infrastructure.security.JwtAuthenticationFilter;
import com.example.identity.interfaces.rest.exception.AuthExceptionHandler;
import com.example.identity.interfaces.rest.mapper.AuthRestMapperImpl;
import com.example.user.domain.model.Role;
import com.example.user.domain.model.UserId;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters =
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {JwtAuthenticationFilter.class, SecurityConfig.class}))
@Import({AuthExceptionHandler.class, AuthRestMapperImpl.class, AuthTestSecurityConfig.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoginUseCase loginUseCase;

    @MockBean
    private RegisterUseCase registerUseCase;

    @MockBean
    private GetCurrentUserUseCase getCurrentUserUseCase;

    @MockBean
    private ForgotPasswordUseCase forgotPasswordUseCase;

    @MockBean
    private ResetPasswordUseCase resetPasswordUseCase;

    @Test
    void login_validCredentials_returns200() throws Exception {
        UserId userId = UserId.of(UUID.randomUUID().toString());
        when(loginUseCase.execute(any()))
                .thenReturn(AuthResult.builder()
                        .token("jwt-token")
                        .userId(userId)
                        .email("admin@nexuscrm.com")
                        .firstName("System")
                        .lastName("Admin")
                        .role(Role.ADMIN)
                        .build());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", "admin@nexuscrm.com",
                                "password", "Admin123!"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.email").value("admin@nexuscrm.com"));
    }

    @Test
    void login_invalidCredentials_returns401() throws Exception {
        when(loginUseCase.execute(any())).thenThrow(new InvalidCredentialsException());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", "admin@nexuscrm.com",
                                "password", "wrong"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void me_authenticated_returns200() throws Exception {
        UserId userId = UserId.of(UUID.randomUUID().toString());
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        new AuthenticatedUserPrincipal(userId, "admin@nexuscrm.com", Role.ADMIN),
                        null));
        when(getCurrentUserUseCase.execute(any()))
                .thenReturn(CurrentUserResult.builder()
                        .id(userId)
                        .email("admin@nexuscrm.com")
                        .firstName("System")
                        .lastName("Admin")
                        .role(Role.ADMIN)
                        .build());

        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("admin@nexuscrm.com"));

        SecurityContextHolder.clearContext();
    }
}
