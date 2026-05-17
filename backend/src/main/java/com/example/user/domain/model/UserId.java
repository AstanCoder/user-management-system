package com.example.user.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Typed identifier for a user aggregate.
 */
public final class UserId {

    private final UUID value;

    private UserId(UUID value) {
        this.value = Objects.requireNonNull(value, "value must not be null");
    }

    /**
     * Creates a user id from a UUID string.
     *
     * @param id uuid string
     * @return user id
     */
    public static UserId of(String id) {
        return new UserId(UUID.fromString(id));
    }

    /**
     * Creates a user id from a UUID.
     *
     * @param id uuid
     * @return user id
     */
    public static UserId of(UUID id) {
        return new UserId(id);
    }

    /**
     * Generates a new random user id.
     *
     * @return new user id
     */
    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserId userId = (UserId) o;
        return value.equals(userId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
