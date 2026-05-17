package com.example.contact.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
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

    @Schema(description = "Contact UUID", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @Schema(description = "First name", example = "Jane")
    private String firstName;

    @Schema(description = "Last name", example = "Doe")
    private String lastName;

    @Schema(description = "Email address", example = "jane.doe@example.com")
    private String email;

    @Schema(description = "Phone number or null", example = "+14155552671")
    private String phone;

    @Schema(description = "Creation timestamp (ISO-8601)", example = "2026-05-17T10:00:00Z")
    private Instant createdAt;

    @Schema(description = "Last update timestamp (ISO-8601)", example = "2026-05-17T10:00:00Z")
    private Instant updatedAt;
}
