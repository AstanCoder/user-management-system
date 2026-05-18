package com.example.contact.domain.query;

import com.example.contact.domain.model.Activity;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PagedActivities {

    List<Activity> content;
    long totalElements;
    int totalPages;
    int page;
    int size;
}
