package com.example.contact.application.port.in;

import com.example.contact.application.command.NoteResult;
import com.example.contact.domain.valueobject.ContactId;
import java.util.List;

/**
 * Lists notes for a contact.
 */
public interface ListNotesUseCase {

    /**
     * Returns notes for contact.
     *
     * @param contactId contact id
     * @return notes
     */
    List<NoteResult> execute(ContactId contactId);
}
