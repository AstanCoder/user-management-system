package com.example.contact.domain.query;

import lombok.Builder;
import lombok.Value;

/**
 * Domain criteria for paginated contact search.
 */
@Value
@Builder
public class ContactSearchCriteria {

    String search;
    int page;
    int size;
    String sort;
}
