package com.example.contact.domain.exception;

import com.example.contact.domain.valueobject.ActivityId;

public class ActivityNotFoundException extends RuntimeException {

    public ActivityNotFoundException(ActivityId activityId) {
        super("Activity not found: " + activityId.value());
    }
}
