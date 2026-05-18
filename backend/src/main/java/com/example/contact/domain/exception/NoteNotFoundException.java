package com.example.contact.domain.exception;

import com.example.contact.domain.valueobject.NoteId;

public class NoteNotFoundException extends RuntimeException {

    public NoteNotFoundException(NoteId noteId) {
        super("Note not found: " + noteId.value());
    }
}
