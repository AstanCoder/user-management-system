package com.example.contact.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.contact.application.command.ContactResult;
import com.example.contact.application.command.CreateContactCommand;
import com.example.contact.application.mapper.ContactApplicationMapper;
import com.example.contact.domain.model.Contact;
import com.example.contact.domain.port.ContactRepository;
import com.example.contact.domain.port.DomainEventPublisher;
import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.domain.valueobject.Email;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private DomainEventPublisher eventPublisher;

    @Mock
    private ContactApplicationMapper contactApplicationMapper;

    @InjectMocks
    private CreateContactService createContactService;

    @Test
    void execute_persistsContact() {
        when(contactRepository.findByEmail(any(Email.class))).thenReturn(Optional.empty());
        when(contactRepository.save(any(Contact.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        ContactId id = ContactId.generate();
        when(contactApplicationMapper.toResult(any(Contact.class)))
                .thenReturn(ContactResult.builder()
                        .id(id)
                        .firstName("Jane")
                        .lastName("Doe")
                        .email("jane@example.com")
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build());

        ContactResult result = createContactService.execute(CreateContactCommand.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane@example.com")
                .build());

        assertThat(result.getFirstName()).isEqualTo("Jane");
        assertThat(result.getEmail()).isEqualTo("jane@example.com");
        verify(contactRepository).save(any(Contact.class));
        verify(eventPublisher).publish(any());
    }
}
