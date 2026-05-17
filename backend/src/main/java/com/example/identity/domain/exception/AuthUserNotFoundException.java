package com.example.identity.domain.exception;

import com.example.user.domain.model.UserId;

/**
 * Thrown when an authenticated user cannot be found.
 */
public class AuthUserNotFoundException extends RuntimeException {

    public AuthUserNotFoundException(UserId id) {
        super("Authenticated user not found: " + id.value());
    }
}
