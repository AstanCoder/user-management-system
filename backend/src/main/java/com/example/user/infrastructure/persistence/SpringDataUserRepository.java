package com.example.user.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link UserJpaEntity}.
 */
public interface SpringDataUserRepository extends JpaRepository<UserJpaEntity, UUID> {

    /**
     * Finds user by email ignoring case.
     *
     * @param email email
     * @return optional entity
     */
    Optional<UserJpaEntity> findByEmailIgnoreCase(String email);

    /**
     * Counts users with given status.
     *
     * @param status status string
     * @return count
     */
    long countByStatus(String status);
}
