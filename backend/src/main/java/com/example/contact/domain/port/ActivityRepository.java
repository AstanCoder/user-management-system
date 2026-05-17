package com.example.contact.domain.port;

import com.example.contact.domain.model.Activity;
import com.example.contact.domain.valueobject.ContactId;
import java.util.List;

/**
 * Outbound port for activity persistence.
 */
public interface ActivityRepository {

    /**
     * Lists activities for a contact newest first.
     *
     * @param contactId contact id
     * @return activities
     */
    List<Activity> findByContactId(ContactId contactId);

    /**
     * Saves an activity.
     *
     * @param activity activity
     * @return saved activity
     */
    Activity save(Activity activity);
}
