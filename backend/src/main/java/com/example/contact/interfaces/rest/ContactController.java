package com.example.contact.interfaces.rest;

import com.example.contact.application.command.ContactResult;
import com.example.contact.application.port.in.CreateContactUseCase;
import com.example.contact.application.port.in.DeleteContactUseCase;
import com.example.contact.application.port.in.GetContactUseCase;
import com.example.contact.application.port.in.ListContactsUseCase;
import com.example.contact.application.port.in.UpdateContactUseCase;
import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.interfaces.rest.dto.ContactResponse;
import com.example.contact.interfaces.rest.dto.CreateContactRequest;
import com.example.contact.interfaces.rest.dto.UpdateContactRequest;
import com.example.contact.interfaces.rest.mapper.ContactRestMapper;
import com.example.contact.interfaces.rest.sanitizer.ContactRequestSanitizer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST driving adapter for contact CRUD operations. Delegates all business logic to use cases.
 */
@Tag(name = "Contacts", description = "Create, read, update, and delete contacts in the contact list")
@RestController
@RequestMapping("/api/contacts")
@Validated
public class ContactController {

    private final ListContactsUseCase listContactsUseCase;
    private final GetContactUseCase getContactUseCase;
    private final CreateContactUseCase createContactUseCase;
    private final UpdateContactUseCase updateContactUseCase;
    private final DeleteContactUseCase deleteContactUseCase;
    private final ContactRestMapper mapper;

    public ContactController(
            ListContactsUseCase listContactsUseCase,
            GetContactUseCase getContactUseCase,
            CreateContactUseCase createContactUseCase,
            UpdateContactUseCase updateContactUseCase,
            DeleteContactUseCase deleteContactUseCase,
            ContactRestMapper mapper) {
        this.listContactsUseCase = listContactsUseCase;
        this.getContactUseCase = getContactUseCase;
        this.createContactUseCase = createContactUseCase;
        this.updateContactUseCase = updateContactUseCase;
        this.deleteContactUseCase = deleteContactUseCase;
        this.mapper = mapper;
    }

    /**
     * Returns all contacts in the system.
     *
     * @return list of contacts
     */
    @Operation(
            summary = "List all contacts",
            description = "Retrieves every contact ordered by creation time (newest first). "
                    + "Returns an empty array when no contacts exist.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Contacts retrieved successfully",
                content =
                        @Content(
                                array = @ArraySchema(schema = @Schema(implementation = ContactResponse.class)))),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected server error",
                content = @Content(schema = @Schema(implementation = com.example.contact.interfaces.rest.dto.ErrorResponse.class)))
    })
    @GetMapping
    public List<ContactResponse> list() {
        return listContactsUseCase.execute().stream().map(mapper::toResponse).toList();
    }

    /**
     * Returns a single contact by id.
     *
     * @param id contact UUID
     * @return contact resource
     */
    @Operation(
            summary = "Get contact by id",
            description = "Loads one contact by its UUID path parameter. Returns 404 when the contact does not exist.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Contact found"),
        @ApiResponse(responseCode = "404", description = "Contact not found"),
        @ApiResponse(responseCode = "400", description = "Invalid UUID format")
    })
    @GetMapping("/{id}")
    public ContactResponse getById(
            @Parameter(description = "Contact UUID", example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable
                    String id) {
        ContactResult result = getContactUseCase.execute(ContactId.of(id));
        return mapper.toResponse(result);
    }

    /**
     * Creates a new contact from the request body.
     *
     * @param request create payload
     * @return created contact with HTTP 201
     */
    @Operation(
            summary = "Create a contact",
            description =
                    "Validates and sanitizes the request body, persists a new contact, and returns the created resource. "
                            + "Email is stored in lowercase. Returns 409 if the email already exists.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Contact created"),
        @ApiResponse(responseCode = "400", description = "Validation failed"),
        @ApiResponse(responseCode = "409", description = "Duplicate email"),
        @ApiResponse(responseCode = "500", description = "Unexpected server error")
    })
    @PostMapping
    public ResponseEntity<ContactResponse> create(@Valid @RequestBody CreateContactRequest request) {
        CreateContactRequest sanitized = ContactRequestSanitizer.sanitize(request);
        ContactResult result = createContactUseCase.execute(mapper.toCommand(sanitized));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    /**
     * Updates an existing contact.
     *
     * @param id contact UUID
     * @param request update payload
     * @return updated contact
     */
    @Operation(
            summary = "Update a contact",
            description =
                    "Replaces mutable fields on an existing contact after validation and sanitization. "
                            + "Returns 404 when the id is unknown and 409 on duplicate email.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Contact updated"),
        @ApiResponse(responseCode = "400", description = "Validation failed"),
        @ApiResponse(responseCode = "404", description = "Contact not found"),
        @ApiResponse(responseCode = "409", description = "Duplicate email")
    })
    @PutMapping("/{id}")
    public ContactResponse update(
            @Parameter(description = "Contact UUID") @PathVariable String id,
            @Valid @RequestBody UpdateContactRequest request) {
        UpdateContactRequest sanitized = ContactRequestSanitizer.sanitize(request);
        ContactResult result = updateContactUseCase.execute(mapper.toUpdateCommand(id, sanitized));
        return mapper.toResponse(result);
    }

    /**
     * Deletes a contact by id.
     *
     * @param id contact UUID
     * @return empty response with HTTP 204
     */
    @Operation(
            summary = "Delete a contact",
            description = "Permanently removes a contact by UUID. Returns 404 when the contact does not exist.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Contact deleted"),
        @ApiResponse(responseCode = "404", description = "Contact not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "Contact UUID") @PathVariable String id) {
        deleteContactUseCase.execute(ContactId.of(id));
        return ResponseEntity.noContent().build();
    }
}
