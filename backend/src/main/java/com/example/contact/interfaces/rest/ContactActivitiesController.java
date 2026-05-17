package com.example.contact.interfaces.rest;

import com.example.contact.application.command.ActivityResult;
import com.example.contact.application.port.in.ListActivitiesUseCase;
import com.example.contact.application.port.in.LogActivityUseCase;
import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.interfaces.rest.dto.ActivityResponse;
import com.example.contact.interfaces.rest.dto.LogActivityRequest;
import com.example.identity.interfaces.rest.SecurityContextAccessor;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Contact Activities")
@RestController
@RequestMapping("/api/contacts/{contactId}/activities")
@Validated
public class ContactActivitiesController {

    private final LogActivityUseCase logActivityUseCase;
    private final ListActivitiesUseCase listActivitiesUseCase;

    public ContactActivitiesController(
            LogActivityUseCase logActivityUseCase, ListActivitiesUseCase listActivitiesUseCase) {
        this.logActivityUseCase = logActivityUseCase;
        this.listActivitiesUseCase = listActivitiesUseCase;
    }

    @GetMapping
    public List<ActivityResponse> list(@PathVariable String contactId) {
        return listActivitiesUseCase.execute(ContactId.of(contactId)).stream()
                .map(this::toResponse)
                .toList();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public ResponseEntity<ActivityResponse> log(
            @PathVariable String contactId, @Valid @RequestBody LogActivityRequest request) {
        ActivityResult result = logActivityUseCase.execute(
                ContactId.of(contactId),
                SecurityContextAccessor.currentUserId().value(),
                request.getActivityType(),
                request.getDescription(),
                request.getOccurredAt());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(result));
    }

    private ActivityResponse toResponse(ActivityResult result) {
        return ActivityResponse.builder()
                .id(result.getId())
                .activityType(result.getActivityType())
                .description(result.getDescription())
                .authorUserId(result.getAuthorUserId())
                .occurredAt(result.getOccurredAt())
                .createdAt(result.getCreatedAt())
                .build();
    }
}
