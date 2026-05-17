package com.example.contact.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataTagRepository extends JpaRepository<TagJpaEntity, UUID> {

    Optional<TagJpaEntity> findByNameIgnoreCase(String name);
}
