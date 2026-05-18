package com.example.contact.domain.query;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ActivityListCriteria {

    String activityType;
    int page;
    int size;
}
