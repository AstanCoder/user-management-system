package com.example.contact.application.service;

import com.example.contact.application.command.NoteResult;
import com.example.contact.application.mapper.ContactApplicationMapper;
import com.example.contact.application.port.in.ListNotesUseCase;
import com.example.contact.domain.port.NoteRepository;
import com.example.contact.domain.valueobject.ContactId;

public final class ListNotesService implements ListNotesUseCase {

    private final NoteRepository noteRepository;
    private final ContactApplicationMapper mapper;

    public ListNotesService(NoteRepository noteRepository, ContactApplicationMapper mapper) {
        this.noteRepository = noteRepository;
        this.mapper = mapper;
    }

    @Override
    public java.util.List<NoteResult> execute(ContactId contactId) {
        return noteRepository.findByContactId(contactId).stream().map(mapper::toNoteResult).toList();
    }
}
