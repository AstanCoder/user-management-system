package com.example.contact.domain.query;

import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 * Domain criteria for paginated contact search.
 */
@Value
@Builder
public class ContactSearchCriteria {

    String search;
    String email;
    String phone;
    List<String> tagNames;
    int page;
    int size;
    String sort;
}
