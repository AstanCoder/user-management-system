package com.example.contact.application.port.in;

import com.example.contact.domain.valueobject.ActivityId;
import com.example.contact.domain.valueobject.ContactId;

public interface DeleteActivityUseCase {

    void execute(ContactId contactId, ActivityId activityId);
}
