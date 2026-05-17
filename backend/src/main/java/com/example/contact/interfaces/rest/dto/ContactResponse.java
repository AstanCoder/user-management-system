package com.example.contact.interfaces.rest.dto;

import com.example.contact.domain.model.ContactStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * HTTP response representing a contact resource.
 */
@Schema(name = "ContactResponse", description = "Contact resource returned by the API")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactResponse {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String company;
    private String jobTitle;
    private String street;
    private String city;
    private String postalCode;
    private String country;
    private String avatarUrl;
    private ContactStatus status;
    private UUID assignedToUserId;
    private Instant createdAt;
    private Instant updatedAt;
    private List<NoteResponse> notes;
    private List<ActivityResponse> activities;
    private List<TagResponse> tags;
}
