package com.example.contact.application.port.in;

import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.domain.valueobject.NoteId;

public interface DeleteNoteUseCase {

    void execute(ContactId contactId, NoteId noteId);
}
