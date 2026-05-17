package com.example.contact.application.port.in;

import com.example.contact.application.command.ContactResult;
import com.example.contact.application.command.CreateContactCommand;
import com.example.contact.domain.exception.DuplicateEmailException;
import com.example.contact.domain.exception.InvalidContactDataException;

/**
 * Inbound port for creating a contact.
 */
public interface CreateContactUseCase {

    /**
     * Creates and persists a new contact.
     *
     * @param command create command
     * @return created contact result
     * @throws InvalidContactDataException when value objects are invalid
     * @throws DuplicateEmailException when email already exists
     */
    ContactResult execute(CreateContactCommand command);
}
