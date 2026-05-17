package com.example.contact.application.service;

import com.example.contact.application.command.ContactResult;
import com.example.contact.application.command.CreateContactCommand;
import com.example.contact.application.mapper.ContactApplicationMapper;
import com.example.contact.application.port.in.CreateContactUseCase;
import com.example.contact.domain.event.ContactCreated;
import com.example.contact.domain.model.Contact;
import com.example.contact.domain.model.ContactStatus;
import com.example.contact.domain.port.ContactRepository;
import com.example.contact.domain.port.DomainEventPublisher;
import com.example.contact.domain.service.ContactUniquenessPolicy;
import com.example.contact.domain.valueobject.Address;
import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.domain.valueobject.Email;
import com.example.contact.domain.valueobject.PhoneNumber;

/**
 * Creates contacts by orchestrating domain rules and persistence ports.
 */
public final class CreateContactService implements CreateContactUseCase {

    private final ContactRepository contactRepository;
    private final DomainEventPublisher eventPublisher;
    private final ContactUniquenessPolicy uniquenessPolicy;
    private final ContactApplicationMapper contactApplicationMapper;

    public CreateContactService(
            ContactRepository contactRepository,
            DomainEventPublisher eventPublisher,
            ContactApplicationMapper contactApplicationMapper) {
        this.contactRepository = contactRepository;
        this.eventPublisher = eventPublisher;
        this.contactApplicationMapper = contactApplicationMapper;
        this.uniquenessPolicy = new ContactUniquenessPolicy(contactRepository);
    }

    @Override
    public ContactResult execute(CreateContactCommand command) {
        Email email = Email.create(command.getEmail());
        PhoneNumber phone = PhoneNumber.createOptional(command.getPhone());
        uniquenessPolicy.assertUniqueForCreate(email);
        Address address = Address.createOptional(
                command.getStreet(), command.getCity(), command.getPostalCode(), command.getCountry());
        ContactStatus status = command.getStatus() != null ? command.getStatus() : ContactStatus.ACTIVE;
        Contact contact = Contact.create(
                ContactId.generate(),
                command.getFirstName(),
                command.getLastName(),
                email,
                phone,
                command.getCompany(),
                command.getJobTitle(),
                address,
                status,
                command.getAssignedToUserId());
        Contact saved = contactRepository.save(contact);
        eventPublisher.publish(ContactCreated.now(saved.id()));
        return contactApplicationMapper.toResult(saved);
    }
}
