package com.example.contact.application.port.in;

import com.example.contact.application.command.ActivityResult;
import com.example.contact.domain.valueobject.ContactId;
import java.time.Instant;
import java.util.UUID;

/**
 * Logs an activity on a contact.
 */
public interface LogActivityUseCase {

    /**
     * Creates activity.
     *
     * @param contactId contact id
     * @param authorUserId author
     * @param activityType type
     * @param description description
     * @param occurredAt occurred at
     * @return activity result
     */
    ActivityResult execute(
            ContactId contactId,
            UUID authorUserId,
            String activityType,
            String description,
            Instant occurredAt);
}
