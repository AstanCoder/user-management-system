package com.example.identity.domain.exception;

/**
 * Thrown when an invitation token is invalid or already consumed.
 */
public class InvalidInvitationTokenException extends RuntimeException {

    public InvalidInvitationTokenException() {
        super("Invalid or already used invitation token");
    }
}
