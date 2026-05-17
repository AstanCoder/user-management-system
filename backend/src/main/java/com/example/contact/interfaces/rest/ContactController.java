package com.example.contact.interfaces.rest;

import com.example.contact.application.command.ContactPageResult;
import com.example.contact.application.command.ContactResult;
import com.example.contact.application.command.ContactSearchQuery;
import com.example.contact.application.port.in.CreateContactUseCase;
import com.example.contact.application.port.in.DeleteContactUseCase;
import com.example.contact.application.port.in.GetContactUseCase;
import com.example.contact.application.port.in.ListContactsUseCase;
import com.example.contact.application.port.in.UpdateContactUseCase;
import com.example.contact.application.port.in.UploadAvatarUseCase;
import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.interfaces.rest.dto.ContactPageResponse;
import com.example.contact.interfaces.rest.dto.ContactResponse;
import com.example.contact.interfaces.rest.dto.CreateContactRequest;
import com.example.contact.interfaces.rest.dto.UpdateContactRequest;
import com.example.contact.interfaces.rest.mapper.ContactRestMapper;
import com.example.contact.interfaces.rest.sanitizer.ContactRequestSanitizer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    private final UploadAvatarUseCase uploadAvatarUseCase;
    private final ContactRestMapper mapper;

    public ContactController(
            ListContactsUseCase listContactsUseCase,
            GetContactUseCase getContactUseCase,
            CreateContactUseCase createContactUseCase,
            UpdateContactUseCase updateContactUseCase,
            DeleteContactUseCase deleteContactUseCase,
            UploadAvatarUseCase uploadAvatarUseCase,
            ContactRestMapper mapper) {
        this.listContactsUseCase = listContactsUseCase;
        this.getContactUseCase = getContactUseCase;
        this.createContactUseCase = createContactUseCase;
        this.updateContactUseCase = updateContactUseCase;
        this.deleteContactUseCase = deleteContactUseCase;
        this.uploadAvatarUseCase = uploadAvatarUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "List contacts with pagination")
    @GetMapping
    public ContactPageResponse list(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sort) {
        ContactSearchQuery query = mapper.toSearchQuery(search, page, size, sort);
        ContactPageResult result = listContactsUseCase.execute(query);
        return mapper.toPageResponse(result);
    }

    @Operation(summary = "Get contact by id")
    @GetMapping("/{id}")
    public ContactResponse getById(@Parameter(description = "Contact UUID") @PathVariable String id) {
        ContactResult result = getContactUseCase.execute(ContactId.of(id));
        return mapper.toResponse(result);
    }

    @Operation(summary = "Create a contact")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public ResponseEntity<ContactResponse> create(@Valid @RequestBody CreateContactRequest request) {
        CreateContactRequest sanitized = ContactRequestSanitizer.sanitize(request);
        ContactResult result = createContactUseCase.execute(mapper.toCommand(sanitized));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @Operation(summary = "Update a contact")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public ContactResponse update(
            @PathVariable String id, @Valid @RequestBody UpdateContactRequest request) {
        UpdateContactRequest sanitized = ContactRequestSanitizer.sanitize(request);
        ContactResult result = updateContactUseCase.execute(mapper.toUpdateCommand(id, sanitized));
        return mapper.toResponse(result);
    }

    @Operation(summary = "Delete a contact")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        deleteContactUseCase.execute(ContactId.of(id));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Upload contact avatar")
    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public ContactResponse uploadAvatar(@PathVariable String id, @RequestParam("file") MultipartFile file)
            throws java.io.IOException {
        ContactResult result = uploadAvatarUseCase.execute(
                ContactId.of(id),
                file.getContentType() != null ? file.getContentType() : "application/octet-stream",
                file.getBytes());
        return mapper.toResponse(result);
    }
}
