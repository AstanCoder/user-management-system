package com.example.identity.infrastructure.persistence;

import com.example.identity.domain.port.InvitationTokenRepository;
import com.example.user.domain.model.UserId;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * JPA adapter for invitation tokens.
 */
@Component
public class JpaInvitationTokenRepositoryAdapter implements InvitationTokenRepository {

    private final SpringDataInvitationTokenRepository repository;

    public JpaInvitationTokenRepositoryAdapter(SpringDataInvitationTokenRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(UserId userId, String token) {
        InvitationTokenJpaEntity entity = InvitationTokenJpaEntity.builder()
                .id(UUID.randomUUID())
                .userId(userId.value())
                .token(token)
                .createdAt(Instant.now())
                .build();
        repository.save(entity);
    }

    @Override
    public Optional<UserId> findValidToken(String token) {
        return repository.findByToken(token).flatMap(entity -> {
            if (entity.getUsedAt() != null) {
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
