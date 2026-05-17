package com.example.contact.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

/**
 * Typed identifier for an activity.
 */
public final class ActivityId {

    private final UUID value;

    private ActivityId(UUID value) {
        this.value = Objects.requireNonNull(value, "value must not be null");
    }

    /**
     * Creates from string.
     *
     * @param id uuid string
     * @return activity id
     */
    public static ActivityId of(String id) {
        return new ActivityId(UUID.fromString(id));
    }

    /**
     * Generates new id.
     *
     * @return activity id
     */
    public static ActivityId generate() {
        return new ActivityId(UUID.randomUUID());
    }

    public UUID value() {
        return value;
    }
}
