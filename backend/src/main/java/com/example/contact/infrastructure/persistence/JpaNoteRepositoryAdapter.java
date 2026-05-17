package com.example.contact.infrastructure.persistence;

import com.example.contact.domain.model.Note;
import com.example.contact.domain.port.NoteRepository;
import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.domain.valueobject.NoteId;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class JpaNoteRepositoryAdapter implements NoteRepository {

    private final SpringDataNoteRepository repository;

    public JpaNoteRepositoryAdapter(SpringDataNoteRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Note> findByContactId(ContactId contactId) {
        return repository.findByContactIdOrderByCreatedAtDesc(contactId.value()).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Note save(Note note) {
        NoteJpaEntity saved = repository.save(toEntity(note));
        return toDomain(saved);
    }

    private Note toDomain(NoteJpaEntity entity) {
        return Note.restore(
                NoteId.of(entity.getId().toString()),
                ContactId.of(entity.getContactId().toString()),
                entity.getAuthorUserId(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    private NoteJpaEntity toEntity(Note note) {
        return NoteJpaEntity.builder()
                .id(note.id().value())
                .contactId(note.contactId().value())
                .authorUserId(note.authorUserId())
                .content(note.content())
                .createdAt(note.createdAt())
                .updatedAt(note.updatedAt())
                .build();
    }
}
