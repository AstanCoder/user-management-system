package com.example.contact.infrastructure.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataActivityRepository extends JpaRepository<ActivityJpaEntity, UUID> {

    List<ActivityJpaEntity> findByContactIdOrderByOccurredAtDesc(UUID contactId);

    Page<ActivityJpaEntity> findByContactIdOrderByOccurredAtDesc(UUID contactId, Pageable pageable);

    Page<ActivityJpaEntity> findByContactIdAndActivityTypeIgnoreCaseOrderByOccurredAtDesc(
            UUID contactId, String activityType, Pageable pageable);
}
