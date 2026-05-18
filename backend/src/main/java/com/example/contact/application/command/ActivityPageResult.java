package com.example.contact.application.command;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ActivityPageResult {

    List<ActivityResult> content;
    long totalElements;
    int totalPages;
    int page;
    int size;
}
