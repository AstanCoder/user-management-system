package com.example.contact.infrastructure.persistence;

import com.example.contact.domain.model.Contact;
import com.example.contact.shared.mapper.DomainTypeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Maps between domain {@link Contact} aggregates and {@link ContactJpaEntity} using MapStruct.
 */
@Mapper(componentModel = "spring")
public abstract class ContactPersistenceMapper {

    @Autowired
    protected DomainTypeMapper domainTypeMapper;

    /**
     * Converts a domain aggregate to a JPA entity via Lombok builder.
     *
     * @param contact domain contact
     * @return jpa entity
     */
    @Mapping(target = "id", expression = "java(domainTypeMapper.contactIdToUuid(contact.id()))")
    @Mapping(target = "firstName", expression = "java(contact.firstName())")
    @Mapping(target = "lastName", expression = "java(contact.lastName())")
    @Mapping(target = "email", expression = "java(contact.email().value())")
    @Mapping(
            target = "phone",
            expression = "java(contact.phone() == null ? null : contact.phone().value())")
    @Mapping(target = "createdAt", expression = "java(contact.createdAt())")
    @Mapping(target = "updatedAt", expression = "java(contact.updatedAt())")
    public abstract ContactJpaEntity toEntity(Contact contact);

    /**
     * Converts a JPA entity to a domain aggregate.
     *
     * @param entity jpa entity
     * @return domain contact
     */
    public Contact toDomain(ContactJpaEntity entity) {
        return domainTypeMapper.toContact(entity);
    }
}
