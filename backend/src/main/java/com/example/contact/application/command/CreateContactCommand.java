package com.example.contact.application.command;

import com.example.contact.domain.model.ContactStatus;
import java.util.UUID;
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
    String company;
    String jobTitle;
    String street;
    String city;
    String postalCode;
    String country;
    ContactStatus status;
    UUID assignedToUserId;
}
