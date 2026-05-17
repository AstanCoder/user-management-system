package com.example.contact.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

/**
 * Typed identity for a {@link com.example.contact.domain.model.Contact} aggregate.
 */
public final class ContactId {

    private final UUID value;

    private ContactId(UUID value) {
        this.value = Objects.requireNonNull(value, "id must not be null");
    }

    /**
     * Creates an identifier from an existing UUID string.
     *
     * @param raw uuid string
     * @return contact id
     * @throws IllegalArgumentException when raw is not a valid UUID
     */
    public static ContactId of(String raw) {
        return new ContactId(UUID.fromString(raw));
    }

    /**
     * Generates a new random identifier.
     *
     * @return new contact id
     */
    public static ContactId generate() {
        return new ContactId(UUID.randomUUID());
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactId that)) {
            return false;
        }
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
