package com.example.user.domain.exception;

import com.example.user.domain.model.UserId;

/**
 * Thrown when a user cannot be found by id.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(UserId id) {
        super("User not found: " + id.value());
    }
}
