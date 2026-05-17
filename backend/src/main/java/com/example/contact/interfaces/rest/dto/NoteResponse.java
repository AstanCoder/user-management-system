package com.example.contact.interfaces.rest.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NoteResponse {

    String id;
    String content;
    UUID authorUserId;
    Instant createdAt;
    Instant updatedAt;
}
