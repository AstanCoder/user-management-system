package com.example.contact.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

/**
 * Typed identifier for a tag.
 */
public final class TagId {

    private final UUID value;

    private TagId(UUID value) {
        this.value = Objects.requireNonNull(value, "value must not be null");
    }

    /**
     * Creates from string.
     *
     * @param id uuid string
     * @return tag id
     */
    public static TagId of(String id) {
        return new TagId(UUID.fromString(id));
    }

    /**
     * Generates new id.
     *
     * @return tag id
     */
    public static TagId generate() {
        return new TagId(UUID.randomUUID());
    }

    public UUID value() {
        return value;
    }
}
