package com.example.contact.infrastructure.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataNoteRepository extends JpaRepository<NoteJpaEntity, UUID> {

    List<NoteJpaEntity> findByContactIdOrderByCreatedAtDesc(UUID contactId);
}
