package com.example.contact.application.port.in;

import com.example.contact.application.command.ContactResult;
import com.example.contact.application.command.UpdateContactCommand;
import com.example.contact.domain.exception.ContactNotFoundException;
import com.example.contact.domain.exception.DuplicateEmailException;
import com.example.contact.domain.exception.InvalidContactDataException;

/**
 * Inbound port for updating a contact.
 */
public interface UpdateContactUseCase {

    /**
     * Updates an existing contact.
     *
     * @param command update command
     * @return updated contact result
     * @throws ContactNotFoundException when contact does not exist
     * @throws InvalidContactDataException when value objects are invalid
     * @throws DuplicateEmailException when email conflicts
     */
    ContactResult execute(UpdateContactCommand command);
}
