package com.example.contact.application.service;

import com.example.contact.application.port.in.DeleteNoteUseCase;
import com.example.contact.domain.exception.NoteNotFoundException;
import com.example.contact.domain.model.Note;
import com.example.contact.domain.port.NoteRepository;
import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.domain.valueobject.NoteId;

public final class DeleteNoteService implements DeleteNoteUseCase {

    private final NoteRepository noteRepository;

    public DeleteNoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public void execute(ContactId contactId, NoteId noteId) {
        Note note = noteRepository.findById(noteId).orElseThrow(() -> new NoteNotFoundException(noteId));
        if (!note.contactId().equals(contactId)) {
            throw new NoteNotFoundException(noteId);
        }
        noteRepository.delete(noteId);
    }
}
