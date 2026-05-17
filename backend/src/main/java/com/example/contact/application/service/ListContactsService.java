package com.example.contact.application.service;

import com.example.contact.application.command.ContactResult;
import com.example.contact.application.mapper.ContactApplicationMapper;
import com.example.contact.application.port.in.ListContactsUseCase;
import com.example.contact.domain.port.ContactRepository;
import java.util.Comparator;
import java.util.List;

/**
 * Lists all contacts from the repository port.
 */
public final class ListContactsService implements ListContactsUseCase {

    private final ContactRepository contactRepository;
    private final ContactApplicationMapper contactApplicationMapper;

    public ListContactsService(
            ContactRepository contactRepository, ContactApplicationMapper contactApplicationMapper) {
        this.contactRepository = contactRepository;
        this.contactApplicationMapper = contactApplicationMapper;
    }

    @Override
    public List<ContactResult> execute() {
        return contactRepository.findAll().stream()
                .sorted(Comparator.comparing(
                                com.example.contact.domain.model.Contact::createdAt)
                        .reversed())
                .map(contactApplicationMapper::toResult)
                .toList();
    }
}
