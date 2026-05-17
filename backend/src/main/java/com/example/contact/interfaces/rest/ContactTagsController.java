package com.example.contact.interfaces.rest;

import com.example.contact.application.command.TagResult;
import com.example.contact.application.port.in.AssignTagsUseCase;
import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.interfaces.rest.dto.AssignTagsRequest;
import com.example.contact.interfaces.rest.dto.TagResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Contact Tags")
@RestController
@RequestMapping("/api/contacts/{contactId}/tags")
@Validated
public class ContactTagsController {

    private final AssignTagsUseCase assignTagsUseCase;

    public ContactTagsController(AssignTagsUseCase assignTagsUseCase) {
        this.assignTagsUseCase = assignTagsUseCase;
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public List<TagResponse> assign(@PathVariable String contactId, @RequestBody AssignTagsRequest request) {
        return assignTagsUseCase
                .execute(ContactId.of(contactId), request.getTagNames())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private TagResponse toResponse(TagResult result) {
        return TagResponse.builder().id(result.getId()).name(result.getName()).build();
    }
}
