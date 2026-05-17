package com.example.contact.domain.model;

import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.domain.valueobject.NoteId;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Note entity attached to a contact.
 */
public final class Note {

    private final NoteId id;
    private final ContactId contactId;
    private final UUID authorUserId;
    private String content;
    private final Instant createdAt;
    private Instant updatedAt;

    private Note(
            NoteId id,
            ContactId contactId,
            UUID authorUserId,
            String content,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.contactId = contactId;
        this.authorUserId = authorUserId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Creates a new note.
     *
     * @param id note id
     * @param contactId contact id
     * @param authorUserId author user id or null
     * @param content note text
     * @return note
     */
    public static Note create(NoteId id, ContactId contactId, UUID authorUserId, String content) {
        Instant now = Instant.now();
        return new Note(id, contactId, authorUserId, requireContent(content), now, now);
    }

    /**
     * Rehydrates from persistence.
     *
     * @param id id
     * @param contactId contact id
     * @param authorUserId author
     * @param content content
     * @param createdAt created
     * @param updatedAt updated
     * @return note
     */
    public static Note restore(
            NoteId id,
            ContactId contactId,
            UUID authorUserId,
            String content,
            Instant createdAt,
            Instant updatedAt) {
        return new Note(id, contactId, authorUserId, content, createdAt, updatedAt);
    }

    private static String requireContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("content must not be blank");
        }
        return content.trim();
    }

    public NoteId id() {
        return id;
    }

    public ContactId contactId() {
        return contactId;
    }

    public UUID authorUserId() {
        return authorUserId;
    }

    public String content() {
        return content;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Note note = (Note) o;
        return id.equals(note.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
