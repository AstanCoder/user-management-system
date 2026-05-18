package com.example.contact.interfaces.rest.exception;

import com.example.contact.domain.exception.ActivityAlreadyConfirmedException;
import com.example.contact.domain.exception.ActivityDeletionNotAllowedException;
import com.example.contact.domain.exception.ActivityNotFoundException;
import com.example.contact.domain.exception.ContactNotFoundException;
import com.example.contact.domain.exception.DuplicateEmailException;
import com.example.contact.domain.exception.InvalidContactDataException;
import com.example.contact.domain.exception.NoteNotFoundException;
import com.example.contact.interfaces.rest.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Maps domain and validation exceptions to HTTP error responses documented in OpenAPI.
 */
@RestControllerAdvice(basePackages = "com.example.contact.interfaces.rest")
public class ContactExceptionHandler {

    /**
     * Handles bean validation failures on request DTOs.
     *
     * @param ex validation exception
     * @param request HTTP request
     * @return 400 error response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> ErrorResponse.FieldError.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .build())
                .toList();
        return buildError(HttpStatus.BAD_REQUEST, "Validation failed", request, fieldErrors);
    }

    /**
     * Handles invalid domain value object input propagated from use cases.
     *
     * @param ex domain validation exception
     * @param request HTTP request
     * @return 400 error response
     */
    @ExceptionHandler(InvalidContactDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidData(
            InvalidContactDataException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    /**
     * Handles missing contact resources.
     *
     * @param ex not found exception
     * @param request HTTP request
     * @return 404 error response
     */
    @ExceptionHandler(ContactNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ContactNotFoundException ex, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler({ActivityNotFoundException.class, NoteNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleResourceNotFound(RuntimeException ex, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    /**
     * Handles duplicate email conflicts.
     *
     * @param ex duplicate email exception
     * @param request HTTP request
     * @return 409 error response
     */
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateEmailException ex, HttpServletRequest request) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), request, null);
    }

    @ExceptionHandler(ActivityAlreadyConfirmedException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyConfirmed(
            ActivityAlreadyConfirmedException ex, HttpServletRequest request) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), request, null);
    }

    @ExceptionHandler(ActivityDeletionNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleDeletionNotAllowed(
            ActivityDeletionNotAllowedException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableBody(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, "Malformed JSON request body", request, null);
    }

    /**
     * Handles unexpected errors without exposing stack traces.
     *
     * @param ex exception
     * @param request HTTP request
     * @return 500 error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", request, null);
    }

    private ResponseEntity<ErrorResponse> buildError(
            HttpStatus status, String message, HttpServletRequest request, List<ErrorResponse.FieldError> errors) {
        ErrorResponse body = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .errors(errors)
                .build();
        return ResponseEntity.status(status).body(body);
    }
}
