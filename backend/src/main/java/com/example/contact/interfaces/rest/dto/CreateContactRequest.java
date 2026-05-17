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
 * HTTP request body for creating a contact via POST /api/contacts.
 */
@Schema(name = "CreateContactRequest", description = "Payload to create a new contact")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateContactRequest {

    @Schema(description = "First name of the contact", example = "Jane", maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "firstName must not be blank")
    @Size(min = 1, max = 100)
    @Pattern(regexp = "^[\\p{L} '\\-]+$", message = "firstName contains invalid characters")
    private String firstName;

    @Schema(description = "Last name of the contact", example = "Doe", maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "lastName must not be blank")
    @Size(min = 1, max = 100)
    @Pattern(regexp = "^[\\p{L} '\\-]+$", message = "lastName contains invalid characters")
    private String lastName;

    @Schema(
            description = "Primary email address; trimmed and stored in lowercase",
            example = "jane.doe@example.com",
            maxLength = 255,
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "email must not be blank")
    @Email(message = "email must be valid")
    @Size(max = 255)
    private String email;

    @Schema(description = "Optional phone in E.164-like format", example = "+14155552671", maxLength = 30)
    @Size(max = 30)
    @Pattern(regexp = "^[+0-9]{7,30}$", message = "phone format is invalid")
    private String phone;
}
