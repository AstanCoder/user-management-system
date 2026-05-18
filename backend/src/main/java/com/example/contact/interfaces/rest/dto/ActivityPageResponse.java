package com.example.contact.interfaces.rest.dto;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ActivityPageResponse {

    List<ActivityResponse> content;
    long totalElements;
    int totalPages;
    int page;
    int size;
}
