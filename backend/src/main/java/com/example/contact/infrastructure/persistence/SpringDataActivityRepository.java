package com.example.contact.infrastructure.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataActivityRepository extends JpaRepository<ActivityJpaEntity, UUID> {

    List<ActivityJpaEntity> findByContactIdOrderByOccurredAtDesc(UUID contactId);
}
