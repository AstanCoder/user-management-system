package com.example.identity.infrastructure.persistence;

import com.example.identity.domain.port.PasswordResetTokenRepository;
import com.example.identity.infrastructure.security.SecureTokenHasher;
import com.example.user.domain.model.UserId;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * JPA adapter for password reset tokens.
 */
@Component
public class JpaPasswordResetTokenRepositoryAdapter implements PasswordResetTokenRepository {

    private final SpringDataPasswordResetTokenRepository repository;
    private final SecureTokenHasher tokenHasher;

    public JpaPasswordResetTokenRepositoryAdapter(
            SpringDataPasswordResetTokenRepository repository, SecureTokenHasher tokenHasher) {
        this.repository = repository;
        this.tokenHasher = tokenHasher;
    }

    @Override
    public void save(UserId userId, String token, Instant expiresAt) {
        String tokenHash = tokenHasher.hash(token);
        PasswordResetTokenJpaEntity entity = PasswordResetTokenJpaEntity.builder()
                .id(UUID.randomUUID())
                .userId(userId.value())
                .token(tokenHash)
                .expiresAt(expiresAt)
                .createdAt(Instant.now())
                .build();
        repository.save(entity);
    }

    @Override
    public Optional<UserId> findValidToken(String token) {
        String tokenHash = tokenHasher.hash(token);
        return repository.findFirstByTokenIn(List.of(tokenHash, token)).flatMap(entity -> {
            if (entity.getUsedAt() != null || entity.getExpiresAt().isBefore(Instant.now())) {
                return Optional.empty();
            }
            return Optional.of(UserId.of(entity.getUserId()));
        });
    }

    @Override
    public void markUsed(String token) {
        String tokenHash = tokenHasher.hash(token);
        repository.findFirstByTokenIn(List.of(tokenHash, token)).ifPresent(entity -> {
            entity.setUsedAt(Instant.now());
            repository.save(entity);
        });
    }

    @Override
    public void deleteExpiredUnused(Instant now) {
        repository.deleteByUsedAtIsNullAndExpiresAtBefore(now);
    }
}
