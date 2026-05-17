package com.example.contact.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link ContactJpaEntity}.
 */
public interface SpringDataContactRepository extends JpaRepository<ContactJpaEntity, UUID> {

  Optional<ContactJpaEntity> findByEmailIgnoreCase(String email);
}
