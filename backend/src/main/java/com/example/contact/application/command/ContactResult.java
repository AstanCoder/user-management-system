package com.example.contact.application.command;

import com.example.contact.domain.model.ContactStatus;
import com.example.contact.domain.valueobject.ContactId;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
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
    String company;
    String jobTitle;
    String street;
    String city;
    String postalCode;
    String country;
    String avatarUrl;
    ContactStatus status;
    UUID assignedToUserId;
    Instant createdAt;
    Instant updatedAt;
    List<NoteResult> notes;
    List<ActivityResult> activities;
    List<TagResult> tags;
}
