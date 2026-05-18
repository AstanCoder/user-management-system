package com.example.contact.domain.port;

import com.example.contact.domain.model.Note;
import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.domain.valueobject.NoteId;
import java.util.List;
import java.util.Optional;

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

    Optional<Note> findById(NoteId noteId);

    /**
     * Saves a note.
     *
     * @param note note
     * @return saved note
     */
    Note save(Note note);

    void delete(NoteId noteId);
}
