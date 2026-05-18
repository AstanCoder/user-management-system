package com.example.identity.interfaces.rest.exception;

import com.example.contact.interfaces.rest.dto.ErrorResponse;
import com.example.identity.application.exception.RateLimitExceededException;
import com.example.identity.domain.exception.AuthUserNotFoundException;
import com.example.identity.domain.exception.InvalidCredentialsException;
import com.example.identity.domain.exception.InvalidInvitationTokenException;
import com.example.identity.domain.exception.InvalidPasswordResetTokenException;
import com.example.identity.domain.exception.UserAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Maps identity domain exceptions to HTTP responses.
 */
@RestControllerAdvice(basePackages = "com.example.identity.interfaces.rest")
public class AuthExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> invalidCredentials(
            InvalidCredentialsException ex, HttpServletRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> userExists(UserAlreadyExistsException ex, HttpServletRequest request) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ErrorResponse> rateLimited(RateLimitExceededException ex, HttpServletRequest request) {
        return buildError(HttpStatus.TOO_MANY_REQUESTS, ex.getMessage(), request);
    }

    @ExceptionHandler({
            AuthUserNotFoundException.class,
            InvalidPasswordResetTokenException.class,
            InvalidInvitationTokenException.class
    })
    public ResponseEntity<ErrorResponse> notFound(RuntimeException ex, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    private ResponseEntity<ErrorResponse> buildError(HttpStatus status, String message, HttpServletRequest request) {
        ErrorResponse body = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(status).body(body);
    }
}
