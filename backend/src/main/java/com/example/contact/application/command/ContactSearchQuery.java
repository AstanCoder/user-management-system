package com.example.contact.application.command;

import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 * Paginated contact list query parameters.
 */
@Value
@Builder
public class ContactSearchQuery {

    String search;
    String email;
    String phone;
    List<String> tagNames;
    int page;
    int size;
    String sort;
}
