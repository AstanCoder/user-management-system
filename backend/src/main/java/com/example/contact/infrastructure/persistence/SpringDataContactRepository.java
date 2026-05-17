package com.example.contact.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for contacts.
 */
public interface SpringDataContactRepository
        extends JpaRepository<ContactJpaEntity, UUID>, JpaSpecificationExecutor<ContactJpaEntity> {

    /**
     * Finds contact by email ignoring case.
     *
     * @param email email
     * @return optional entity
     */
    Optional<ContactJpaEntity> findByEmailIgnoreCase(String email);
}
