package com.example.contact.application.port.in;

import com.example.contact.application.command.ActivityResult;
import com.example.contact.domain.valueobject.ContactId;
import java.util.List;

/**
 * Lists activities for a contact.
 */
public interface ListActivitiesUseCase {

    /**
     * Returns activities.
     *
     * @param contactId contact id
     * @return activities
     */
    List<ActivityResult> execute(ContactId contactId);
}
