package com.example.identity.interfaces.rest;

import com.example.identity.application.command.AuthResult;
import com.example.identity.application.command.CurrentUserResult;
import com.example.identity.application.port.in.ForgotPasswordUseCase;
import com.example.identity.application.port.in.GetCurrentUserUseCase;
import com.example.identity.application.port.in.LoginUseCase;
import com.example.identity.application.port.in.CompleteInvitationUseCase;
import com.example.identity.application.port.in.RegisterUseCase;
import com.example.identity.application.port.in.ResetPasswordUseCase;
import com.example.identity.application.port.out.AuthRateLimiter;
import com.example.identity.domain.exception.InvalidCredentialsException;
import com.example.identity.interfaces.rest.dto.AuthResponse;
import com.example.identity.interfaces.rest.dto.CompleteInvitationRequest;
import com.example.identity.interfaces.rest.dto.CurrentUserResponse;
import com.example.identity.interfaces.rest.dto.ForgotPasswordRequest;
import com.example.identity.interfaces.rest.dto.LoginRequest;
import com.example.identity.interfaces.rest.dto.RegisterRequest;
import com.example.identity.interfaces.rest.dto.ResetPasswordRequest;
import com.example.identity.interfaces.rest.mapper.AuthRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST driving adapter for authentication endpoints.
 */
@Tag(name = "Auth", description = "Login, registration, and password reset")
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final CompleteInvitationUseCase completeInvitationUseCase;
    private final RegisterUseCase registerUseCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;
    private final ForgotPasswordUseCase forgotPasswordUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;
    private final AuthRateLimiter authRateLimiter;
    private final AuthRestMapper mapper;

    public AuthController(
            LoginUseCase loginUseCase,
            CompleteInvitationUseCase completeInvitationUseCase,
            RegisterUseCase registerUseCase,
            GetCurrentUserUseCase getCurrentUserUseCase,
            ForgotPasswordUseCase forgotPasswordUseCase,
            ResetPasswordUseCase resetPasswordUseCase,
            AuthRateLimiter authRateLimiter,
            AuthRestMapper mapper) {
        this.loginUseCase = loginUseCase;
        this.completeInvitationUseCase = completeInvitationUseCase;
        this.registerUseCase = registerUseCase;
        this.getCurrentUserUseCase = getCurrentUserUseCase;
        this.forgotPasswordUseCase = forgotPasswordUseCase;
        this.resetPasswordUseCase = resetPasswordUseCase;
        this.authRateLimiter = authRateLimiter;
        this.mapper = mapper;
    }

    /**
     * Authenticates user and returns JWT.
     *
     * @param request login credentials
     * @return auth response
     */
    @Operation(summary = "Login")
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        String clientIp = resolveClientIp(httpRequest);
        authRateLimiter.checkLoginAllowed(clientIp, request.getEmail());
        try {
            AuthResult result = loginUseCase.execute(mapper.toCommand(request));
            return mapper.toResponse(result);
        } catch (InvalidCredentialsException exception) {
            authRateLimiter.onLoginFailure(clientIp, request.getEmail());
            throw exception;
        }
    }

    /**
     * Completes invitation registration and returns JWT.
     *
     * @param request completion data
     * @return auth response
     */
    @Operation(summary = "Complete invitation")
    @PostMapping("/complete-invitation")
    public AuthResponse completeInvitation(@Valid @RequestBody CompleteInvitationRequest request) {
        AuthResult result = completeInvitationUseCase.execute(mapper.toCommand(request));
        return mapper.toResponse(result);
    }

    /**
     * Registers a new user.
     *
     * @param request registration data
     * @return auth response with token
     */
    @Operation(summary = "Register")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResult result = registerUseCase.execute(mapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    /**
     * Returns the current authenticated user.
     *
     * @return current user profile
     */
    @Operation(summary = "Current user")
    @GetMapping("/me")
    public CurrentUserResponse me() {
        CurrentUserResult result = getCurrentUserUseCase.execute(SecurityContextAccessor.currentUserId());
        return mapper.toResponse(result);
    }

    /**
     * Initiates password reset email.
     *
     * @param request email
     * @return accepted
     */
    @Operation(summary = "Forgot password")
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request, HttpServletRequest httpRequest) {
        authRateLimiter.checkForgotPasswordAllowed(resolveClientIp(httpRequest), request.getEmail());
        forgotPasswordUseCase.execute(request.getEmail());
        return ResponseEntity.accepted().build();
    }

    /**
     * Resets password with token.
     *
     * @param request reset payload
     * @return no content
     */
    @Operation(summary = "Reset password")
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        resetPasswordUseCase.execute(request.getToken(), request.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    /**
     * Stateless logout acknowledgement.
     *
     * @return no content
     */
    @Operation(summary = "Logout")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent().build();
    }

    private String resolveClientIp(HttpServletRequest httpRequest) {
        String forwardedFor = httpRequest.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return httpRequest.getRemoteAddr();
    }
}
