package com.example.contact.domain.model;

import com.example.contact.domain.valueobject.TagId;
import java.time.Instant;
import java.util.Objects;

/**
 * Tag entity for categorizing contacts.
 */
public final class Tag {

    private final TagId id;
    private final String name;
    private final Instant createdAt;

    private Tag(TagId id, String name, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    /**
     * Creates a tag.
     *
     * @param id tag id
     * @param name tag name
     * @return tag
     */
    public static Tag create(TagId id, String name) {
        return new Tag(id, requireName(name), Instant.now());
    }

    /**
     * Rehydrates from persistence.
     *
     * @param id id
     * @param name name
     * @param createdAt created at
     * @return tag
     */
    public static Tag restore(TagId id, String name, Instant createdAt) {
        return new Tag(id, name, createdAt);
    }

    private static String requireName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        return name.trim();
    }

    public TagId id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Instant createdAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tag tag = (Tag) o;
        return id.equals(tag.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
