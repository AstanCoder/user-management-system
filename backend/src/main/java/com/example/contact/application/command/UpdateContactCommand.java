package com.example.contact.application.command;

import com.example.contact.domain.valueobject.ContactId;
import lombok.Builder;
import lombok.Value;

/**
 * Input for updating a contact use case.
 */
@Value
@Builder
public class UpdateContactCommand {

    ContactId id;
    String firstName;
    String lastName;
    String email;
    String phone;
}
