package com.example.contact.application.port.in;

import com.example.contact.domain.exception.ContactNotFoundException;
import com.example.contact.domain.valueobject.ContactId;

/**
 * Inbound port for deleting a contact.
 */
public interface DeleteContactUseCase {

    /**
     * Deletes a contact by id.
     *
     * @param id contact id
     * @throws ContactNotFoundException when contact does not exist
     */
    void execute(ContactId id);
}
