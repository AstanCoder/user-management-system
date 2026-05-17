package com.example.contact.infrastructure.persistence;

import com.example.contact.domain.model.Tag;
import com.example.contact.domain.port.TagRepository;
import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.domain.valueobject.TagId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class JpaTagRepositoryAdapter implements TagRepository {

    private final SpringDataTagRepository tagRepository;
    private final SpringDataContactTagRepository contactTagRepository;

    public JpaTagRepositoryAdapter(
            SpringDataTagRepository tagRepository, SpringDataContactTagRepository contactTagRepository) {
        this.tagRepository = tagRepository;
        this.contactTagRepository = contactTagRepository;
    }

    @Override
    public List<Tag> findByContactId(ContactId contactId) {
        return contactTagRepository.findByContactId(contactId.value()).stream()
                .map(ct -> tagRepository.findById(ct.getTagId()).orElseThrow())
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return tagRepository.findByNameIgnoreCase(name.trim()).map(this::toDomain);
    }

    @Override
    public Tag save(Tag tag) {
        return toDomain(tagRepository.save(toEntity(tag)));
    }

    @Override
    public void assignToContact(ContactId contactId, TagId tagId) {
        contactTagRepository.save(new ContactTagJpaEntity(contactId.value(), tagId.value()));
    }

    @Override
    public void clearContactTags(ContactId contactId) {
        contactTagRepository.deleteByContactId(contactId.value());
    }

    private Tag toDomain(TagJpaEntity entity) {
        return Tag.restore(TagId.of(entity.getId().toString()), entity.getName(), entity.getCreatedAt());
    }

    private TagJpaEntity toEntity(Tag tag) {
        return TagJpaEntity.builder()
                .id(tag.id().value())
                .name(tag.name())
                .createdAt(tag.createdAt())
                .build();
    }
}
