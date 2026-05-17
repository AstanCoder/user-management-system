package com.example.contact.interfaces.rest.dto;

import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 * Paginated contact list HTTP response.
 */
@Value
@Builder
public class ContactPageResponse {

    List<ContactResponse> content;
    long totalElements;
    int totalPages;
    int page;
    int size;
}
