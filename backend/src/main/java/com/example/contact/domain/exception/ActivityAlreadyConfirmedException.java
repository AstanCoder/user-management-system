package com.example.contact.domain.exception;

import com.example.contact.domain.valueobject.ActivityId;

public class ActivityAlreadyConfirmedException extends RuntimeException {

    public ActivityAlreadyConfirmedException(ActivityId activityId) {
        super("Activity is already confirmed: " + activityId.value());
    }
}
