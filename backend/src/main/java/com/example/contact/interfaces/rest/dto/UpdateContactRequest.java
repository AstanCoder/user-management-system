package com.example.contact.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * HTTP request body for updating a contact via PUT /api/contacts/{id}.
 */
@Schema(name = "UpdateContactRequest", description = "Payload to update an existing contact")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateContactRequest {

    @Schema(description = "First name", example = "Jane", maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Size(min = 1, max = 100)
    @Pattern(regexp = "^[\\p{L} '\\-]+$", message = "firstName contains invalid characters")
    private String firstName;

    @Schema(description = "Last name", example = "Doe", maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Size(min = 1, max = 100)
    @Pattern(regexp = "^[\\p{L} '\\-]+$", message = "lastName contains invalid characters")
    private String lastName;

    @Schema(description = "Email address", example = "jane.doe@example.com", maxLength = 255, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @Schema(description = "Optional phone", example = "+14155552671", maxLength = 30)
    @Size(max = 30)
    @Pattern(regexp = "^[+0-9]{7,30}$", message = "phone format is invalid")
    private String phone;

    @Schema(description = "Company name", maxLength = 200)
    @Size(max = 200)
    private String company;

    @Schema(description = "Job title", maxLength = 150)
    @Size(max = 150)
    private String jobTitle;

    @Schema(description = "Street address", maxLength = 255)
    @Size(max = 255)
    private String street;

    @Schema(description = "City", maxLength = 100)
    @Size(max = 100)
    private String city;

    @Schema(description = "Postal code", maxLength = 20)
    @Size(max = 20)
    private String postalCode;

    @Schema(description = "Country", maxLength = 100)
    @Size(max = 100)
    private String country;

    @Schema(description = "Contact status", example = "ACTIVE")
    @Pattern(regexp = "^(ACTIVE|INACTIVE|LEAD)$", message = "status is invalid")
    private String status;

    @Schema(description = "Assigned user UUID")
    private String assignedToUserId;
}
