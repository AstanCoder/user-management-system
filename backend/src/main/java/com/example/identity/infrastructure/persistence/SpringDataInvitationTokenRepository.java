package com.example.identity.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for invitation tokens.
 */
public interface SpringDataInvitationTokenRepository
        extends JpaRepository<InvitationTokenJpaEntity, UUID> {

    /**
     * Finds invitation token by value.
     *
     * @param token token value
     * @return optional entity
     */
    Optional<InvitationTokenJpaEntity> findByToken(String token);
}
