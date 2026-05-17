package com.example.contact.application.mapper;

import com.example.contact.application.command.ContactResult;
import com.example.contact.domain.model.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Maps domain {@link Contact} aggregates to application-layer {@link ContactResult} DTOs.
 */
@Mapper(componentModel = "spring")
public interface ContactApplicationMapper {

    /**
     * Maps a domain contact to an application result.
     *
     * @param contact domain aggregate
     * @return application result
     */
    @Mapping(target = "id", expression = "java(contact.id())")
    @Mapping(target = "firstName", expression = "java(contact.firstName())")
    @Mapping(target = "lastName", expression = "java(contact.lastName())")
    @Mapping(target = "email", expression = "java(contact.email().value())")
    @Mapping(
            target = "phone",
            expression = "java(contact.phone() == null ? null : contact.phone().value())")
    @Mapping(target = "createdAt", expression = "java(contact.createdAt())")
    @Mapping(target = "updatedAt", expression = "java(contact.updatedAt())")
    ContactResult toResult(Contact contact);
}
