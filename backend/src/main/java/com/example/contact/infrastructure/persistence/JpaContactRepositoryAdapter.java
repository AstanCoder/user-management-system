package com.example.contact.infrastructure.persistence;

import com.example.contact.domain.model.Contact;
import com.example.contact.domain.port.ContactRepository;
import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.domain.valueobject.Email;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Adapter implementing {@link ContactRepository} with JPA.
 */
@Component
public class JpaContactRepositoryAdapter implements ContactRepository {

    private final SpringDataContactRepository springDataRepository;
    private final ContactPersistenceMapper mapper;

    public JpaContactRepositoryAdapter(
            SpringDataContactRepository springDataRepository, ContactPersistenceMapper mapper) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Contact> findAll() {
        return springDataRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Contact> findById(ContactId id) {
        return springDataRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public Optional<Contact> findByEmail(Email email) {
        return springDataRepository.findByEmailIgnoreCase(email.value()).map(mapper::toDomain);
    }

    @Override
    public Contact save(Contact contact) {
        ContactJpaEntity saved = springDataRepository.save(mapper.toEntity(contact));
        return mapper.toDomain(saved);
    }

    @Override
    public void delete(ContactId id) {
        springDataRepository.deleteById(id.value());
    }
}
