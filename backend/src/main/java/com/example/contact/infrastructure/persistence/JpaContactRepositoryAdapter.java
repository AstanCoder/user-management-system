package com.example.contact.infrastructure.persistence;

import com.example.contact.domain.model.Contact;
import com.example.contact.domain.port.ContactRepository;
import com.example.contact.domain.query.ContactSearchCriteria;
import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.domain.valueobject.Email;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        return springDataRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Contact> search(ContactSearchCriteria criteria) {
        PageRequest pageRequest = PageRequest.of(
                Math.max(criteria.getPage(), 0),
                Math.max(criteria.getSize(), 1),
                resolveSort(criteria.getSort()));
        Page<ContactJpaEntity> page =
                springDataRepository.findAll(ContactSearchSpecification.from(criteria), pageRequest);
        return page.getContent().stream().map(mapper::toDomain).toList();
    }

    @Override
    public long countSearch(ContactSearchCriteria criteria) {
        return springDataRepository.count(ContactSearchSpecification.from(criteria));
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

    private Sort resolveSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }
        String[] parts = sort.split(",");
        String field = parts[0].trim();
        Sort.Direction direction =
                parts.length > 1 && parts[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(direction, field);
    }
}
