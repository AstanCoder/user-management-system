package com.example.contact.interfaces.rest;

import com.example.contact.application.command.ActivityPageResult;
import com.example.contact.application.command.ActivityResult;
import com.example.contact.application.command.ActivitySearchQuery;
import com.example.contact.application.port.in.ConfirmActivityUseCase;
import com.example.contact.application.port.in.DeleteActivityUseCase;
import com.example.contact.application.port.in.ListActivitiesUseCase;
import com.example.contact.application.port.in.LogActivityUseCase;
import com.example.contact.domain.valueobject.ActivityId;
import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.interfaces.rest.dto.ActivityPageResponse;
import com.example.contact.interfaces.rest.dto.ActivityResponse;
import com.example.contact.interfaces.rest.dto.LogActivityRequest;
import com.example.identity.interfaces.rest.SecurityContextAccessor;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Contact Activities")
@RestController
@RequestMapping("/api/contacts/{contactId}/activities")
@Validated
public class ContactActivitiesController {

    private final LogActivityUseCase logActivityUseCase;
    private final ListActivitiesUseCase listActivitiesUseCase;
    private final ConfirmActivityUseCase confirmActivityUseCase;
    private final DeleteActivityUseCase deleteActivityUseCase;

    public ContactActivitiesController(
            LogActivityUseCase logActivityUseCase,
            ListActivitiesUseCase listActivitiesUseCase,
            ConfirmActivityUseCase confirmActivityUseCase,
            DeleteActivityUseCase deleteActivityUseCase) {
        this.logActivityUseCase = logActivityUseCase;
        this.listActivitiesUseCase = listActivitiesUseCase;
        this.confirmActivityUseCase = confirmActivityUseCase;
        this.deleteActivityUseCase = deleteActivityUseCase;
    }

    @GetMapping
    public ActivityPageResponse list(
            @PathVariable String contactId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String activityType) {
        ActivityPageResult result = listActivitiesUseCase.search(
                ContactId.of(contactId),
                ActivitySearchQuery.builder()
                        .activityType(activityType)
                        .page(page)
                        .size(size)
                        .build());
        return ActivityPageResponse.builder()
                .content(result.getContent().stream().map(this::toResponse).toList())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .page(result.getPage())
                .size(result.getSize())
                .build();
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
                request.getOccurredAt(),
                request.getConfirmed() == null || request.getConfirmed());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(result));
    }

    @PatchMapping("/{activityId}/confirm")
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public ActivityResponse confirm(@PathVariable String contactId, @PathVariable String activityId) {
        ActivityResult result = confirmActivityUseCase.execute(ContactId.of(contactId), ActivityId.of(activityId));
        return toResponse(result);
    }

    @DeleteMapping("/{activityId}")
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public ResponseEntity<Void> delete(@PathVariable String contactId, @PathVariable String activityId) {
        deleteActivityUseCase.execute(ContactId.of(contactId), ActivityId.of(activityId));
        return ResponseEntity.noContent().build();
    }

    private ActivityResponse toResponse(ActivityResult result) {
        return ActivityResponse.builder()
                .id(result.getId())
                .activityType(result.getActivityType())
                .description(result.getDescription())
                .authorUserId(result.getAuthorUserId())
                .occurredAt(result.getOccurredAt())
                .createdAt(result.getCreatedAt())
                .confirmed(result.isConfirmed())
                .build();
    }
}
