package com.example.contact.application.service;

import com.example.contact.application.command.ContactResult;
import com.example.contact.application.mapper.ContactApplicationMapper;
import com.example.contact.application.port.in.UploadAvatarUseCase;
import com.example.contact.domain.exception.ContactNotFoundException;
import com.example.contact.domain.model.Contact;
import com.example.contact.domain.port.AvatarStoragePort;
import com.example.contact.domain.port.ContactRepository;
import com.example.contact.domain.valueobject.ContactId;

public final class UploadAvatarService implements UploadAvatarUseCase {

    private final ContactRepository contactRepository;
    private final AvatarStoragePort avatarStoragePort;
    private final ContactApplicationMapper mapper;

    public UploadAvatarService(
            ContactRepository contactRepository,
            AvatarStoragePort avatarStoragePort,
            ContactApplicationMapper mapper) {
        this.contactRepository = contactRepository;
        this.avatarStoragePort = avatarStoragePort;
        this.mapper = mapper;
    }

    @Override
    public ContactResult execute(ContactId contactId, String contentType, byte[] data) {
        Contact contact =
                contactRepository.findById(contactId).orElseThrow(() -> new ContactNotFoundException(contactId));
        String avatarUrl = avatarStoragePort.store(contactId, contentType, data);
        contact.updateAvatarUrl(avatarUrl);
        return mapper.toResult(contactRepository.save(contact));
    }
}
