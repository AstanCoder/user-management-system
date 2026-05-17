package com.example.contact.domain.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.example.contact.domain.exception.DuplicateEmailException;
import com.example.contact.domain.model.Contact;
import com.example.contact.domain.port.ContactRepository;
import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.domain.valueobject.Email;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContactUniquenessPolicyTest {

    @Mock
    private ContactRepository contactRepository;

    @Test
    void assertUniqueForCreate_throwsWhenEmailExists() {
        Email email = Email.create("a@example.com");
        Contact existing = Contact.create(
                ContactId.generate(), "A", "B", email, null, null, null, null, null, null);
        when(contactRepository.findByEmail(email)).thenReturn(Optional.of(existing));

        ContactUniquenessPolicy policy = new ContactUniquenessPolicy(contactRepository);
        assertThatThrownBy(() -> policy.assertUniqueForCreate(email))
                .isInstanceOf(DuplicateEmailException.class);
    }
}
