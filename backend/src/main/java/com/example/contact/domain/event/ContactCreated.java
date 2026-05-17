package com.example.contact.domain.event;

import com.example.contact.domain.valueobject.ContactId;
import java.time.Instant;

/**
 * Domain event emitted after a contact is successfully persisted.
 *
 * @param contactId created contact id
 * @param occurredAt event timestamp
 */
public record ContactCreated(ContactId contactId, Instant occurredAt) {

    /**
     * Creates an event for the given contact id at the current instant.
     *
     * @param contactId contact id
     * @return domain event
     */
    public static ContactCreated now(ContactId contactId) {
        return new ContactCreated(contactId, Instant.now());
    }
}
