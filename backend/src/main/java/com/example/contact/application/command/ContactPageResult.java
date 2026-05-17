package com.example.contact.application.command;

import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 * Paginated contact list result.
 */
@Value
@Builder
public class ContactPageResult {

    List<ContactResult> content;
    long totalElements;
    int totalPages;
    int page;
    int size;
}
