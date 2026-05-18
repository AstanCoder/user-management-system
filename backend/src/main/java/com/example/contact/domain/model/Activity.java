package com.example.contact.domain.model;

import com.example.contact.domain.valueobject.ActivityId;
import com.example.contact.domain.valueobject.ContactId;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Activity log entry for a contact.
 */
public final class Activity {

    private final ActivityId id;
    private final ContactId contactId;
    private final UUID authorUserId;
    private final String activityType;
    private final String description;
    private final Instant occurredAt;
    private final Instant createdAt;
    private final boolean confirmed;

    private Activity(
            ActivityId id,
            ContactId contactId,
            UUID authorUserId,
            String activityType,
            String description,
            Instant occurredAt,
            Instant createdAt,
            boolean confirmed) {
        this.id = id;
        this.contactId = contactId;
        this.authorUserId = authorUserId;
        this.activityType = activityType;
        this.description = description;
        this.occurredAt = occurredAt;
        this.createdAt = createdAt;
        this.confirmed = confirmed;
    }

    /**
     * Creates a new activity.
     *
     * @param id activity id
     * @param contactId contact id
     * @param authorUserId author
     * @param activityType type
     * @param description description
     * @param occurredAt when it occurred
     * @return activity
     */
    public static Activity create(
            ActivityId id,
            ContactId contactId,
            UUID authorUserId,
            String activityType,
            String description,
            Instant occurredAt,
            boolean confirmed) {
        return new Activity(
                id,
                contactId,
                authorUserId,
                require(activityType, "activityType"),
                require(description, "description"),
                occurredAt != null ? occurredAt : Instant.now(),
                Instant.now(),
                confirmed);
    }

    /**
     * Rehydrates from persistence.
     *
     * @param id id
     * @param contactId contact id
     * @param authorUserId author
     * @param activityType type
     * @param description description
     * @param occurredAt occurred at
     * @param createdAt created at
     * @return activity
     */
    public static Activity restore(
            ActivityId id,
            ContactId contactId,
            UUID authorUserId,
            String activityType,
            String description,
            Instant occurredAt,
            Instant createdAt,
            boolean confirmed) {
        return new Activity(id, contactId, authorUserId, activityType, description, occurredAt, createdAt, confirmed);
    }

    public Activity confirm() {
        if (confirmed) {
            return this;
        }
        return new Activity(id, contactId, authorUserId, activityType, description, occurredAt, createdAt, true);
    }

    private static String require(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }

    public ActivityId id() {
        return id;
    }

    public ContactId contactId() {
        return contactId;
    }

    public UUID authorUserId() {
        return authorUserId;
    }

    public String activityType() {
        return activityType;
    }

    public String description() {
        return description;
    }

    public Instant occurredAt() {
        return occurredAt;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public boolean confirmed() {
        return confirmed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Activity activity = (Activity) o;
        return id.equals(activity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
