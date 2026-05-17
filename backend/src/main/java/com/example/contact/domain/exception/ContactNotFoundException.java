package com.example.contact.domain.exception;

import com.example.contact.domain.valueobject.ContactId;

/**
 * Raised when a contact cannot be found by identifier.
 */
public class ContactNotFoundException extends RuntimeException {

    public ContactNotFoundException(ContactId id) {
        super("Contact not found: " + id);
    }
}
