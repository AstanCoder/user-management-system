package com.example.contact.application.command;

import lombok.Builder;
import lombok.Value;

/**
 * Paginated contact list query parameters.
 */
@Value
@Builder
public class ContactSearchQuery {

    String search;
    int page;
    int size;
    String sort;
}
