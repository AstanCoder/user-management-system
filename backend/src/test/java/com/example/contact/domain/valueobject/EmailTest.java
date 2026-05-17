package com.example.contact.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.contact.domain.exception.InvalidContactDataException;
import org.junit.jupiter.api.Test;

class EmailTest {

    @Test
    void create_normalizesEmail() {
        Email email = Email.create("  User@Example.COM  ");
        assertThat(email.value()).isEqualTo("user@example.com");
    }

    @Test
    void create_rejectsInvalidFormat() {
        assertThatThrownBy(() -> Email.create("not-an-email"))
                .isInstanceOf(InvalidContactDataException.class);
    }
}
