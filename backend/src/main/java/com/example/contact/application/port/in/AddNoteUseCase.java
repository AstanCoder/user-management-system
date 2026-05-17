package com.example.contact.application.port.in;

import com.example.contact.application.command.NoteResult;
import com.example.contact.domain.valueobject.ContactId;
import java.util.UUID;

/**
 * Adds a note to a contact.
 */
public interface AddNoteUseCase {

    /**
     * Creates a note.
     *
     * @param contactId contact id
     * @param authorUserId author id
     * @param content note text
     * @return note result
     */
    NoteResult execute(ContactId contactId, UUID authorUserId, String content);
}
