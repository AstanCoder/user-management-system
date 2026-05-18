package com.example.contact.application.command;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ActivitySearchQuery {

    String activityType;
    int page;
    int size;
}
