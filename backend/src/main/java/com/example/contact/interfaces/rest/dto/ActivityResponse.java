package com.example.contact.interfaces.rest.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ActivityResponse {

    String id;
    String activityType;
    String description;
    UUID authorUserId;
    Instant occurredAt;
    Instant createdAt;
    boolean confirmed;
}
