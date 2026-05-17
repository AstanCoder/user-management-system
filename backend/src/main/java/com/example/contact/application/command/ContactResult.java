package com.example.contact.application.command;

import com.example.contact.domain.valueobject.ContactId;
import java.time.Instant;
import lombok.Builder;
import lombok.Value;

/**
 * Application-layer output representing a contact for adapters to map.
 */
@Value
@Builder
public class ContactResult {

    ContactId id;
    String firstName;
    String lastName;
    String email;
    String phone;
    Instant createdAt;
    Instant updatedAt;
}
