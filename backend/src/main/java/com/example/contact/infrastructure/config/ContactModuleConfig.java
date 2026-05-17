package com.example.contact.infrastructure.config;

import com.example.contact.application.mapper.ContactApplicationMapper;
import com.example.contact.application.port.in.CreateContactUseCase;
import com.example.contact.application.port.in.DeleteContactUseCase;
import com.example.contact.application.port.in.GetContactUseCase;
import com.example.contact.application.port.in.ListContactsUseCase;
import com.example.contact.application.port.in.UpdateContactUseCase;
import com.example.contact.application.service.CreateContactService;
import com.example.contact.application.service.DeleteContactService;
import com.example.contact.application.service.GetContactService;
import com.example.contact.application.service.ListContactsService;
import com.example.contact.application.service.UpdateContactService;
import com.example.contact.domain.port.ContactRepository;
import com.example.contact.domain.port.DomainEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Composition root wiring use cases to outbound port implementations.
 */
@Configuration
public class ContactModuleConfig {

    @Bean
    public CreateContactUseCase createContactUseCase(
            ContactRepository contactRepository,
            DomainEventPublisher eventPublisher,
            ContactApplicationMapper contactApplicationMapper) {
        return new CreateContactService(contactRepository, eventPublisher, contactApplicationMapper);
    }

    @Bean
    public ListContactsUseCase listContactsUseCase(
            ContactRepository contactRepository, ContactApplicationMapper contactApplicationMapper) {
        return new ListContactsService(contactRepository, contactApplicationMapper);
    }

    @Bean
    public GetContactUseCase getContactUseCase(
            ContactRepository contactRepository, ContactApplicationMapper contactApplicationMapper) {
        return new GetContactService(contactRepository, contactApplicationMapper);
    }

    @Bean
    public UpdateContactUseCase updateContactUseCase(
            ContactRepository contactRepository, ContactApplicationMapper contactApplicationMapper) {
        return new UpdateContactService(contactRepository, contactApplicationMapper);
    }

    @Bean
    public DeleteContactUseCase deleteContactUseCase(ContactRepository contactRepository) {
        return new DeleteContactService(contactRepository);
    }
}
