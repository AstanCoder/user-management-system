package com.example.identity.domain.exception;

/**
 * Thrown when a password reset token is invalid or expired.
 */
public class InvalidPasswordResetTokenException extends RuntimeException {

    public InvalidPasswordResetTokenException() {
        super("Invalid or expired password reset token");
    }
}
