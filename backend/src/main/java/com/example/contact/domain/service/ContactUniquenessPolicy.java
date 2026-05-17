package com.example.contact.domain.service;

import com.example.contact.domain.exception.DuplicateEmailException;
import com.example.contact.domain.port.ContactRepository;
import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.domain.valueobject.Email;
import java.util.Optional;

/**
 * Domain policy ensuring email uniqueness across contacts.
 */
public final class ContactUniquenessPolicy {

    private final ContactRepository contactRepository;

    public ContactUniquenessPolicy(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    /**
     * Asserts that no other contact owns the email.
     *
     * @param email email to check
     * @throws DuplicateEmailException when another contact uses the email
     */
    public void assertUniqueForCreate(Email email) {
        contactRepository.findByEmail(email).ifPresent(existing -> {
            throw new DuplicateEmailException(email);
        });
    }

    /**
     * Asserts uniqueness when updating, excluding the current contact id.
     *
     * @param email target email
     * @param currentId id of contact being updated
     * @throws DuplicateEmailException when another contact uses the email
     */
    public void assertUniqueForUpdate(Email email, ContactId currentId) {
        Optional<com.example.contact.domain.model.Contact> existing =
                contactRepository.findByEmail(email);
        if (existing.isPresent() && !existing.get().id().equals(currentId)) {
            throw new DuplicateEmailException(email);
        }
    }
}
