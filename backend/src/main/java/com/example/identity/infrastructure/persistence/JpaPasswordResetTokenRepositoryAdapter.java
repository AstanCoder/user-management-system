package com.example.identity.infrastructure.persistence;

import com.example.identity.domain.port.PasswordResetTokenRepository;
import com.example.user.domain.model.UserId;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * JPA adapter for password reset tokens.
 */
@Component
public class JpaPasswordResetTokenRepositoryAdapter implements PasswordResetTokenRepository {

    private final SpringDataPasswordResetTokenRepository repository;

    public JpaPasswordResetTokenRepositoryAdapter(SpringDataPasswordResetTokenRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(UserId userId, String token, Instant expiresAt) {
        PasswordResetTokenJpaEntity entity = PasswordResetTokenJpaEntity.builder()
                .id(UUID.randomUUID())
                .userId(userId.value())
                .token(token)
                .expiresAt(expiresAt)
                .createdAt(Instant.now())
                .build();
        repository.save(entity);
    }

    @Override
    public Optional<UserId> findValidToken(String token) {
        return repository.findByToken(token).flatMap(entity -> {
            if (entity.getUsedAt() != null || entity.getExpiresAt().isBefore(Instant.now())) {
                return Optional.empty();
            }
            return Optional.of(UserId.of(entity.getUserId()));
        });
    }

    @Override
    public void markUsed(String token) {
        repository.findByToken(token).ifPresent(entity -> {
            entity.setUsedAt(Instant.now());
            repository.save(entity);
        });
    }
}
