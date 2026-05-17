package com.example.contact.domain.port;

import com.example.contact.domain.event.ContactCreated;

/**
 * Outbound port for publishing domain events.
 */
public interface DomainEventPublisher {

    /**
     * Publishes a contact created event.
     *
     * @param event domain event
     */
    void publish(ContactCreated event);
}
