package com.example.user.interfaces.rest.exception;

import com.example.contact.interfaces.rest.dto.ErrorResponse;
import com.example.user.domain.exception.DuplicateUserEmailException;
import com.example.user.domain.exception.UserInvitationResendNotAllowedException;
import com.example.user.domain.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Maps user domain exceptions to HTTP responses.
 */
@RestControllerAdvice(basePackages = "com.example.user.interfaces.rest")
public class UserExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(UserNotFoundException ex, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(DuplicateUserEmailException.class)
    public ResponseEntity<ErrorResponse> duplicate(DuplicateUserEmailException ex, HttpServletRequest request) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(UserInvitationResendNotAllowedException.class)
    public ResponseEntity<ErrorResponse> resendNotAllowed(
            UserInvitationResendNotAllowedException ex, HttpServletRequest request) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), request);
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
