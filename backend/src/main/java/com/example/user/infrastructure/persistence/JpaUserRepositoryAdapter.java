package com.example.user.infrastructure.persistence;

import com.example.user.domain.model.User;
import com.example.user.domain.model.UserId;
import com.example.user.domain.model.UserStatus;
import com.example.user.domain.port.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * JPA adapter for {@link UserRepository}.
 */
@Component
public class JpaUserRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository springDataUserRepository;
    private final UserPersistenceMapper mapper;

    public JpaUserRepositoryAdapter(
            SpringDataUserRepository springDataUserRepository, UserPersistenceMapper mapper) {
        this.springDataUserRepository = springDataUserRepository;
        this.mapper = mapper;
    }

    @Override
    public List<User> findAll() {
        return springDataUserRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<User> findById(UserId id) {
        return springDataUserRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springDataUserRepository.findByEmailIgnoreCase(email.trim().toLowerCase()).map(mapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserJpaEntity saved = springDataUserRepository.save(mapper.toEntity(user));
        return mapper.toDomain(saved);
    }

    @Override
    public void delete(UserId id) {
        springDataUserRepository.deleteById(id.value());
    }

    @Override
    public long countAll() {
        return springDataUserRepository.count();
    }

    @Override
    public long countByStatus(UserStatus status) {
        return springDataUserRepository.countByStatus(status.name());
    }
}
