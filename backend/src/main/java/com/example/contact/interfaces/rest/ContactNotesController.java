package com.example.contact.interfaces.rest;

import com.example.contact.application.command.NoteResult;
import com.example.contact.application.port.in.AddNoteUseCase;
import com.example.contact.application.port.in.DeleteNoteUseCase;
import com.example.contact.application.port.in.ListNotesUseCase;
import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.domain.valueobject.NoteId;
import com.example.contact.interfaces.rest.dto.AddNoteRequest;
import com.example.contact.interfaces.rest.dto.NoteResponse;
import com.example.identity.interfaces.rest.SecurityContextAccessor;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Contact Notes")
@RestController
@RequestMapping("/api/contacts/{contactId}/notes")
@Validated
public class ContactNotesController {

    private final AddNoteUseCase addNoteUseCase;
    private final ListNotesUseCase listNotesUseCase;
    private final DeleteNoteUseCase deleteNoteUseCase;

    public ContactNotesController(
            AddNoteUseCase addNoteUseCase, ListNotesUseCase listNotesUseCase, DeleteNoteUseCase deleteNoteUseCase) {
        this.addNoteUseCase = addNoteUseCase;
        this.listNotesUseCase = listNotesUseCase;
        this.deleteNoteUseCase = deleteNoteUseCase;
    }

    @GetMapping
    public List<NoteResponse> list(@PathVariable String contactId) {
        return listNotesUseCase.execute(ContactId.of(contactId)).stream()
                .map(this::toResponse)
                .toList();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public ResponseEntity<NoteResponse> add(
            @PathVariable String contactId, @Valid @RequestBody AddNoteRequest request) {
        NoteResult result = addNoteUseCase.execute(
                ContactId.of(contactId),
                SecurityContextAccessor.currentUserId().value(),
                request.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(result));
    }

    @DeleteMapping("/{noteId}")
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public ResponseEntity<Void> delete(@PathVariable String contactId, @PathVariable String noteId) {
        deleteNoteUseCase.execute(ContactId.of(contactId), NoteId.of(noteId));
        return ResponseEntity.noContent().build();
    }

    private NoteResponse toResponse(NoteResult result) {
        return NoteResponse.builder()
                .id(result.getId())
                .content(result.getContent())
                .authorUserId(result.getAuthorUserId())
                .createdAt(result.getCreatedAt())
                .updatedAt(result.getUpdatedAt())
                .build();
    }
}
