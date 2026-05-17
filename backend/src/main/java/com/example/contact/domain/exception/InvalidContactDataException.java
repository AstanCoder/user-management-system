package com.example.contact.domain.exception;

/**
 * Thrown when contact value objects or invariants are violated.
 */
public class InvalidContactDataException extends RuntimeException {

    public InvalidContactDataException(String message) {
        super(message);
    }
}
