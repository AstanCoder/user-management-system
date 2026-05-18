package com.example.user.infrastructure.persistence;

import com.example.user.domain.model.Role;
import com.example.user.domain.model.User;
import com.example.user.domain.model.UserId;
import com.example.user.domain.model.UserStatus;
import org.springframework.stereotype.Component;

/**
 * Maps between domain users and JPA entities.
 */
@Component
public class UserPersistenceMapper {

    /**
     * Converts domain user to JPA entity.
     *
     * @param user domain user
     * @return jpa entity
     */
    public UserJpaEntity toEntity(User user) {
        return UserJpaEntity.builder()
                .id(user.id().value())
                .email(user.email())
                .passwordHash(user.passwordHash())
                .firstName(user.firstName())
                .lastName(user.lastName())
                .role(user.role().name())
                .status(user.status().name())
                .createdAt(user.createdAt())
                .updatedAt(user.updatedAt())
                .lastActiveAt(user.lastActiveAt())
                .build();
    }

    /**
     * Converts JPA entity to domain user.
     *
     * @param entity jpa entity
     * @return domain user
     */
    public User toDomain(UserJpaEntity entity) {
        return User.restore(
                UserId.of(entity.getId()),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getFirstName(),
                entity.getLastName(),
                Role.valueOf(entity.getRole()),
                UserStatus.valueOf(entity.getStatus()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getLastActiveAt());
    }
}
