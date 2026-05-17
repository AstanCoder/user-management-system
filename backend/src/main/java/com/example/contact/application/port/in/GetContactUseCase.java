package com.example.contact.application.port.in;

import com.example.contact.application.command.ContactResult;
import com.example.contact.domain.exception.ContactNotFoundException;
import com.example.contact.domain.valueobject.ContactId;

/**
 * Inbound port for retrieving a single contact.
 */
public interface GetContactUseCase {

    /**
     * Loads a contact by id.
     *
     * @param id contact id
     * @return contact result
     * @throws ContactNotFoundException when not found
     */
    ContactResult execute(ContactId id);
}
