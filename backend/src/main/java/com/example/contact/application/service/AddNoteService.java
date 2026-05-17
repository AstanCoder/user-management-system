package com.example.contact.application.service;

import com.example.contact.application.command.NoteResult;
import com.example.contact.application.mapper.ContactApplicationMapper;
import com.example.contact.application.port.in.AddNoteUseCase;
import com.example.contact.domain.exception.ContactNotFoundException;
import com.example.contact.domain.model.Note;
import com.example.contact.domain.port.ContactRepository;
import com.example.contact.domain.port.NoteRepository;
import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.domain.valueobject.NoteId;
import java.util.UUID;

public final class AddNoteService implements AddNoteUseCase {

    private final ContactRepository contactRepository;
    private final NoteRepository noteRepository;
    private final ContactApplicationMapper mapper;

    public AddNoteService(
            ContactRepository contactRepository, NoteRepository noteRepository, ContactApplicationMapper mapper) {
        this.contactRepository = contactRepository;
        this.noteRepository = noteRepository;
        this.mapper = mapper;
    }

    @Override
    public NoteResult execute(ContactId contactId, UUID authorUserId, String content) {
        if (contactRepository.findById(contactId).isEmpty()) {
            throw new ContactNotFoundException(contactId);
        }
        Note note = Note.create(NoteId.generate(), contactId, authorUserId, content);
        return mapper.toNoteResult(noteRepository.save(note));
    }
}
