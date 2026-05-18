package com.example.contact.domain.exception;

import com.example.contact.domain.valueobject.ActivityId;

public class ActivityDeletionNotAllowedException extends RuntimeException {

    public ActivityDeletionNotAllowedException(ActivityId activityId) {
        super("Only unconfirmed activities can be deleted: " + activityId.value());
    }
}
