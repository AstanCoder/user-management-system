package com.example.contact.application.service;

import com.example.contact.application.command.ContactResult;
import com.example.contact.application.command.UpdateContactCommand;
import com.example.contact.application.mapper.ContactApplicationMapper;
import com.example.contact.application.port.in.UpdateContactUseCase;
import com.example.contact.domain.exception.ContactNotFoundException;
import com.example.contact.domain.model.Contact;
import com.example.contact.domain.port.ContactRepository;
import com.example.contact.domain.service.ContactUniquenessPolicy;
import com.example.contact.domain.valueobject.Email;
import com.example.contact.domain.valueobject.PhoneNumber;

/**
 * Updates an existing contact aggregate.
 */
public final class UpdateContactService implements UpdateContactUseCase {

    private final ContactRepository contactRepository;
    private final ContactUniquenessPolicy uniquenessPolicy;
    private final ContactApplicationMapper contactApplicationMapper;

    public UpdateContactService(
            ContactRepository contactRepository, ContactApplicationMapper contactApplicationMapper) {
        this.contactRepository = contactRepository;
        this.contactApplicationMapper = contactApplicationMapper;
        this.uniquenessPolicy = new ContactUniquenessPolicy(contactRepository);
    }

    @Override
    public ContactResult execute(UpdateContactCommand command) {
        Contact contact = contactRepository
                .findById(command.getId())
                .orElseThrow(() -> new ContactNotFoundException(command.getId()));
        Email email = Email.create(command.getEmail());
        PhoneNumber phone = PhoneNumber.createOptional(command.getPhone());
        uniquenessPolicy.assertUniqueForUpdate(email, command.getId());
        contact.update(command.getFirstName(), command.getLastName(), email, phone);
        return contactApplicationMapper.toResult(contactRepository.save(contact));
    }
}
