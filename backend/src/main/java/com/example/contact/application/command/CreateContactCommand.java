package com.example.contact.application.command;

import lombok.Builder;
import lombok.Value;

/**
 * Input for creating a contact use case.
 */
@Value
@Builder
public class CreateContactCommand {

    String firstName;
    String lastName;
    String email;
    String phone;
}
