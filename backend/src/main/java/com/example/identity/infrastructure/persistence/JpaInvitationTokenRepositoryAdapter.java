package com.example.identity.infrastructure.persistence;

import com.example.identity.domain.port.InvitationTokenRepository;
import com.example.identity.infrastructure.security.SecureTokenHasher;
import com.example.user.domain.model.UserId;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JPA adapter for invitation tokens.
 */
@Component
public class JpaInvitationTokenRepositoryAdapter implements InvitationTokenRepository {

    private final SpringDataInvitationTokenRepository repository;
    private final SecureTokenHasher tokenHasher;
    private final long invitationTokenExpirationHours;

    public JpaInvitationTokenRepositoryAdapter(
            SpringDataInvitationTokenRepository repository,
            SecureTokenHasher tokenHasher,
            @Value("${app.identity.invitation-token-expiration-hours:24}") long invitationTokenExpirationHours) {
        this.repository = repository;
        this.tokenHasher = tokenHasher;
        this.invitationTokenExpirationHours = invitationTokenExpirationHours;
    }

    @Override
    public void save(UserId userId, String token) {
        String tokenHash = tokenHasher.hash(token);
        Instant now = Instant.now();
        InvitationTokenJpaEntity entity = InvitationTokenJpaEntity.builder()
                .id(UUID.randomUUID())
                .userId(userId.value())
                .token(tokenHash)
                .expiresAt(now.plus(invitationTokenExpirationHours, ChronoUnit.HOURS))
                .createdAt(now)
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
