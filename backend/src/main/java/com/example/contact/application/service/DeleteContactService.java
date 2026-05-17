package com.example.contact.application.service;

import com.example.contact.application.port.in.DeleteContactUseCase;
import com.example.contact.domain.exception.ContactNotFoundException;
import com.example.contact.domain.port.ContactRepository;
import com.example.contact.domain.valueobject.ContactId;

/**
 * Deletes a contact by id when it exists.
 */
public final class DeleteContactService implements DeleteContactUseCase {

    private final ContactRepository contactRepository;

    public DeleteContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public void execute(ContactId id) {
        if (contactRepository.findById(id).isEmpty()) {
            throw new ContactNotFoundException(id);
        }
        contactRepository.delete(id);
    }
}
