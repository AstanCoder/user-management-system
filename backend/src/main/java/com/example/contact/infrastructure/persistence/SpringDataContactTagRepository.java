package com.example.contact.infrastructure.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataContactTagRepository extends JpaRepository<ContactTagJpaEntity, ContactTagJpaEntity.ContactTagId> {

    List<ContactTagJpaEntity> findByContactId(UUID contactId);

    void deleteByContactId(UUID contactId);
}
