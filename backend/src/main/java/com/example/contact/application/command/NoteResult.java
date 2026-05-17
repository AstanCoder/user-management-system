package com.example.contact.application.command;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

/**
 * Application result for a contact note.
 */
@Value
@Builder
public class NoteResult {

    String id;
    String content;
    UUID authorUserId;
    Instant createdAt;
    Instant updatedAt;
}
