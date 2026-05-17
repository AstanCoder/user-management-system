package com.example.user.domain.exception;

/**
 * Thrown when registering or creating a user with an email that already exists.
 */
public class DuplicateUserEmailException extends RuntimeException {

    public DuplicateUserEmailException(String email) {
        super("User email already exists: " + email);
    }
}
