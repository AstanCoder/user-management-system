package com.example.contact.application.service;

import com.example.contact.application.command.ContactResult;
import com.example.contact.application.mapper.ContactApplicationMapper;
import com.example.contact.application.port.in.GetContactUseCase;
import com.example.contact.domain.exception.ContactNotFoundException;
import com.example.contact.domain.port.ContactRepository;
import com.example.contact.domain.valueobject.ContactId;

/**
 * Loads a single contact by id.
 */
public final class GetContactService implements GetContactUseCase {

    private final ContactRepository contactRepository;
    private final ContactApplicationMapper contactApplicationMapper;

    public GetContactService(
            ContactRepository contactRepository, ContactApplicationMapper contactApplicationMapper) {
        this.contactRepository = contactRepository;
        this.contactApplicationMapper = contactApplicationMapper;
    }

    @Override
    public ContactResult execute(ContactId id) {
        return contactRepository
                .findById(id)
                .map(contactApplicationMapper::toResult)
                .orElseThrow(() -> new ContactNotFoundException(id));
    }
}
