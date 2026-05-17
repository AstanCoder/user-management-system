package com.example.contact.domain.port;

import com.example.contact.domain.model.Note;
import com.example.contact.domain.valueobject.ContactId;
import java.util.List;

/**
 * Outbound port for note persistence.
 */
public interface NoteRepository {

    /**
     * Lists notes for a contact newest first.
     *
     * @param contactId contact id
     * @return notes
     */
    List<Note> findByContactId(ContactId contactId);

    /**
     * Saves a note.
     *
     * @param note note
     * @return saved note
     */
    Note save(Note note);
}
