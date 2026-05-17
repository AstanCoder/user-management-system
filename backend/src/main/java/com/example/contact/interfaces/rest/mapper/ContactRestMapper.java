package com.example.contact.interfaces.rest.mapper;

import com.example.contact.application.command.ContactResult;
import com.example.contact.application.command.CreateContactCommand;
import com.example.contact.application.command.UpdateContactCommand;
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

    /**
     * Maps create request to application command.
     *
     * @param request HTTP request
     * @return create command
     */
    CreateContactCommand toCommand(CreateContactRequest request);

    /**
     * Maps update request to application command.
     *
     * @param id contact id path variable
     * @param request HTTP request
     * @return update command
     */
    @Mapping(target = "id", source = "id", qualifiedByName = "stringToContactId")
    UpdateContactCommand toUpdateCommand(String id, UpdateContactRequest request);

    /**
     * Maps application result to HTTP response.
     *
     * @param result application result
     * @return HTTP response DTO
     */
    @Mapping(target = "id", source = "id", qualifiedByName = "contactIdToString")
    ContactResponse toResponse(ContactResult result);
}
