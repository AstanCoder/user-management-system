package com.example.contact.domain.exception;

import com.example.contact.domain.valueobject.Email;

/**
 * Raised when creating or updating a contact would violate unique email constraint.
 */
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(Email email) {
        super("Contact with email already exists: " + email.value());
    }
}
