package com.example.contact.infrastructure.event;

import com.example.contact.domain.event.ContactCreated;
import com.example.contact.domain.port.DomainEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Logs domain events; replace with messaging adapter when needed.
 */
@Component
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(SpringDomainEventPublisher.class);

    @Override
    public void publish(ContactCreated event) {
        log.info("Domain event published: ContactCreated id={} at={}", event.contactId(), event.occurredAt());
    }
}
