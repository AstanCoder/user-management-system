package com.example.contact.application.port.in;

import com.example.contact.application.command.ActivityResult;
import com.example.contact.domain.valueobject.ActivityId;
import com.example.contact.domain.valueobject.ContactId;

public interface ConfirmActivityUseCase {

    ActivityResult execute(ContactId contactId, ActivityId activityId);
}
