package com.example.contact.infrastructure.persistence;

import com.example.contact.domain.model.Contact;
import com.example.contact.domain.valueobject.Address;
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

    @Mapping(target = "id", expression = "java(domainTypeMapper.contactIdToUuid(contact.id()))")
    @Mapping(target = "firstName", expression = "java(contact.firstName())")
    @Mapping(target = "lastName", expression = "java(contact.lastName())")
    @Mapping(target = "email", expression = "java(contact.email().value())")
    @Mapping(
            target = "phone",
            expression = "java(contact.phone() == null ? null : contact.phone().value())")
    @Mapping(target = "company", expression = "java(contact.company())")
    @Mapping(target = "jobTitle", expression = "java(contact.jobTitle())")
    @Mapping(target = "street", expression = "java(addressField(contact.address(), \"street\"))")
    @Mapping(target = "city", expression = "java(addressField(contact.address(), \"city\"))")
    @Mapping(target = "postalCode", expression = "java(addressField(contact.address(), \"postalCode\"))")
    @Mapping(target = "country", expression = "java(addressField(contact.address(), \"country\"))")
    @Mapping(target = "avatarUrl", expression = "java(contact.avatarUrl())")
    @Mapping(target = "status", expression = "java(contact.status().name())")
    @Mapping(target = "assignedToUserId", expression = "java(contact.assignedToUserId())")
    @Mapping(target = "createdAt", expression = "java(contact.createdAt())")
    @Mapping(target = "updatedAt", expression = "java(contact.updatedAt())")
    public abstract ContactJpaEntity toEntity(Contact contact);

    public Contact toDomain(ContactJpaEntity entity) {
        return domainTypeMapper.toContact(entity);
    }

    protected String addressField(Address address, String field) {
        if (address == null) {
            return null;
        }
        return switch (field) {
            case "street" -> address.street();
            case "city" -> address.city();
            case "postalCode" -> address.postalCode();
            case "country" -> address.country();
            default -> null;
        };
    }
}
