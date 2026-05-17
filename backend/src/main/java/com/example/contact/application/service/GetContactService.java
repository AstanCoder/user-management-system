package com.example.contact.application.service;

import com.example.contact.application.command.ContactResult;
import com.example.contact.application.mapper.ContactApplicationMapper;
import com.example.contact.application.port.in.GetContactUseCase;
import com.example.contact.domain.exception.ContactNotFoundException;
import com.example.contact.domain.model.Contact;
import com.example.contact.domain.port.ActivityRepository;
import com.example.contact.domain.port.ContactRepository;
import com.example.contact.domain.port.NoteRepository;
import com.example.contact.domain.port.TagRepository;
import com.example.contact.domain.valueobject.ContactId;

/**
 * Loads a single contact with related notes, activities, and tags.
 */
public final class GetContactService implements GetContactUseCase {

    private final ContactRepository contactRepository;
    private final NoteRepository noteRepository;
    private final ActivityRepository activityRepository;
    private final TagRepository tagRepository;
    private final ContactApplicationMapper contactApplicationMapper;

    public GetContactService(
            ContactRepository contactRepository,
            NoteRepository noteRepository,
            ActivityRepository activityRepository,
            TagRepository tagRepository,
            ContactApplicationMapper contactApplicationMapper) {
        this.contactRepository = contactRepository;
        this.noteRepository = noteRepository;
        this.activityRepository = activityRepository;
        this.tagRepository = tagRepository;
        this.contactApplicationMapper = contactApplicationMapper;
    }

    @Override
    public ContactResult execute(ContactId id) {
        Contact contact =
                contactRepository.findById(id).orElseThrow(() -> new ContactNotFoundException(id));
        return contactApplicationMapper.toDetailResult(
                contact,
                noteRepository.findByContactId(id),
                activityRepository.findByContactId(id),
                tagRepository.findByContactId(id));
    }
}
