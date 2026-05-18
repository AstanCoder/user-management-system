package com.example.contact.application.command;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

/**
 * Application result for a contact activity.
 */
@Value
@Builder
public class ActivityResult {

    String id;
    String activityType;
    String description;
    UUID authorUserId;
    Instant occurredAt;
    Instant createdAt;
    boolean confirmed;
}
