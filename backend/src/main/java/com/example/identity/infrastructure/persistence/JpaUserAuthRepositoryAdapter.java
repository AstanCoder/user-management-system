package com.example.identity.infrastructure.persistence;

import com.example.identity.domain.model.AuthUser;
import com.example.identity.domain.port.UserAuthRepository;
import com.example.user.domain.model.Role;
import com.example.user.domain.model.User;
import com.example.user.domain.model.UserId;
import com.example.user.domain.model.UserStatus;
import com.example.user.infrastructure.persistence.SpringDataUserRepository;
import com.example.user.infrastructure.persistence.UserJpaEntity;
import com.example.user.infrastructure.persistence.UserPersistenceMapper;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Auth repository adapter using shared user JPA entities.
 */
@Component
public class JpaUserAuthRepositoryAdapter implements UserAuthRepository {

    private final SpringDataUserRepository springDataUserRepository;
    private final UserPersistenceMapper userPersistenceMapper;

    public JpaUserAuthRepositoryAdapter(
            SpringDataUserRepository springDataUserRepository, UserPersistenceMapper userPersistenceMapper) {
        this.springDataUserRepository = springDataUserRepository;
        this.userPersistenceMapper = userPersistenceMapper;
    }

    @Override
    public Optional<AuthUser> findByEmail(String email) {
        return springDataUserRepository.findByEmailIgnoreCase(email.trim().toLowerCase()).map(this::toAuthUser);
    }

    @Override
    public Optional<AuthUser> findById(UserId id) {
        return springDataUserRepository.findById(id.value()).map(this::toAuthUser);
    }

    @Override
    public User save(User user) {
        UserJpaEntity saved = springDataUserRepository.save(userPersistenceMapper.toEntity(user));
        return userPersistenceMapper.toDomain(saved);
    }

    @Override
    public void updatePassword(UserId id, String passwordHash) {
        UserJpaEntity entity = springDataUserRepository
                .findById(id.value())
                .orElseThrow(() -> new IllegalStateException("User not found for password update"));
        entity.setPasswordHash(passwordHash);
        if (UserStatus.INVITED.name().equals(entity.getStatus())) {
            entity.setStatus(UserStatus.ACTIVE.name());
        }
        entity.setUpdatedAt(java.time.Instant.now());
        springDataUserRepository.save(entity);
    }

    @Override
    public boolean existsByEmail(String email) {
        return springDataUserRepository.findByEmailIgnoreCase(email.trim().toLowerCase()).isPresent();
    }

    @Override
    public void recordLastActiveAt(UserId id) {
        springDataUserRepository
                .findById(id.value())
                .ifPresent(entity -> {
                    User user = userPersistenceMapper.toDomain(entity);
                    user.recordLastActiveAt();
                    springDataUserRepository.save(userPersistenceMapper.toEntity(user));
                });
    }

    private AuthUser toAuthUser(UserJpaEntity entity) {
        return new AuthUser(
                UserId.of(entity.getId()),
                entity.getEmail(),
                entity.getPasswordHash(),
                Role.valueOf(entity.getRole()),
                UserStatus.valueOf(entity.getStatus()),
                entity.getFirstName(),
                entity.getLastName());
    }
}
