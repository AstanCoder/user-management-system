package com.example.contact.interfaces.rest.mapper;

import com.example.contact.application.command.ContactPageResult;
import com.example.contact.application.command.ContactResult;
import com.example.contact.application.command.ContactSearchQuery;
import com.example.contact.application.command.CreateContactCommand;
import com.example.contact.application.command.UpdateContactCommand;
import com.example.contact.domain.model.ContactStatus;
import com.example.contact.interfaces.rest.dto.ContactPageResponse;
import com.example.contact.interfaces.rest.dto.ContactResponse;
import com.example.contact.interfaces.rest.dto.CreateContactRequest;
import com.example.contact.interfaces.rest.dto.UpdateContactRequest;
import com.example.contact.shared.mapper.DomainTypeMapper;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Maps between REST DTOs and application commands/results using MapStruct.
 */
@Mapper(componentModel = "spring", uses = {DomainTypeMapper.class, AvatarUrlRestMapper.class})
public interface ContactRestMapper {

    @Mapping(target = "status", expression = "java(parseStatus(request.getStatus()))")
    @Mapping(target = "assignedToUserId", expression = "java(parseUuid(request.getAssignedToUserId()))")
    CreateContactCommand toCommand(CreateContactRequest request);

    @Mapping(target = "id", source = "id", qualifiedByName = "stringToContactId")
    @Mapping(target = "status", expression = "java(parseStatus(request.getStatus()))")
    @Mapping(target = "assignedToUserId", expression = "java(parseUuid(request.getAssignedToUserId()))")
    UpdateContactCommand toUpdateCommand(String id, UpdateContactRequest request);

    @Mapping(target = "id", source = "id", qualifiedByName = "contactIdToString")
    @Mapping(target = "avatarUrl", source = "avatarUrl", qualifiedByName = "resolveAvatarUrl")
    ContactResponse toResponse(ContactResult result);

    ContactPageResponse toPageResponse(ContactPageResult result);

    default ContactSearchQuery toSearchQuery(
            String search, String email, String phone, List<String> tagNames, int page, int size, String sort) {
        return ContactSearchQuery.builder()
                .search(search)
                .email(email)
                .phone(phone)
                .tagNames(tagNames)
                .page(page)
                .size(size)
                .sort(sort)
                .build();
    }

    default ContactStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        return ContactStatus.valueOf(status);
    }

    default UUID parseUuid(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return UUID.fromString(value);
    }
}
