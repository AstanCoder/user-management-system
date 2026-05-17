package com.example.identity.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for password reset tokens.
 */
public interface SpringDataPasswordResetTokenRepository
        extends JpaRepository<PasswordResetTokenJpaEntity, UUID> {

    /**
     * Finds token by value.
     *
     * @param token token string
     * @return optional entity
     */
    Optional<PasswordResetTokenJpaEntity> findByToken(String token);
}
