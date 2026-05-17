package com.example.contact.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

/**
 * Typed identifier for a note.
 */
public final class NoteId {

    private final UUID value;

    private NoteId(UUID value) {
        this.value = Objects.requireNonNull(value, "value must not be null");
    }

    /**
     * Creates from string.
     *
     * @param id uuid string
     * @return note id
     */
    public static NoteId of(String id) {
        return new NoteId(UUID.fromString(id));
    }

    /**
     * Generates new id.
     *
     * @return note id
     */
    public static NoteId generate() {
        return new NoteId(UUID.randomUUID());
    }

    public UUID value() {
        return value;
    }
}
