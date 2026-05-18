package com.example.identity.application.exception;

/**
 * Raised when auth endpoint request limits are exceeded.
 */
public class RateLimitExceededException extends RuntimeException {

    public RateLimitExceededException(String message) {
        super(message);
    }
}
