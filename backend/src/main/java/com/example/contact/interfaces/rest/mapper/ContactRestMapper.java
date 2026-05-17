package com.example.contact.interfaces.rest.mapper;

import com.example.contact.application.command.ContactPageResult;
import com.example.contact.application.command.ContactResult;
import com.example.contact.application.command.ContactSearchQuery;
import com.example.contact.application.command.CreateContactCommand;
import com.example.contact.application.command.UpdateContactCommand;
import com.example.contact.interfaces.rest.dto.ContactPageResponse;
import com.example.contact.interfaces.rest.dto.ContactResponse;
import com.example.contact.interfaces.rest.dto.CreateContactRequest;
import com.example.contact.interfaces.rest.dto.UpdateContactRequest;
import com.example.contact.shared.mapper.DomainTypeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Maps between REST DTOs and application commands/results using MapStruct.
 */
@Mapper(componentModel = "spring", uses = DomainTypeMapper.class)
public interface ContactRestMapper {

    CreateContactCommand toCommand(CreateContactRequest request);

    @Mapping(target = "id", source = "id", qualifiedByName = "stringToContactId")
    UpdateContactCommand toUpdateCommand(String id, UpdateContactRequest request);

    @Mapping(target = "id", source = "id", qualifiedByName = "contactIdToString")
    ContactResponse toResponse(ContactResult result);

    ContactPageResponse toPageResponse(ContactPageResult result);

    default ContactSearchQuery toSearchQuery(String search, int page, int size, String sort) {
        return ContactSearchQuery.builder()
                .search(search)
                .page(page)
                .size(size)
                .sort(sort)
                .build();
    }
}
