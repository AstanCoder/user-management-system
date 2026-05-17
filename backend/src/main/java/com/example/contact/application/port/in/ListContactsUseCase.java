package com.example.contact.application.port.in;

import com.example.contact.application.command.ContactPageResult;
import com.example.contact.application.command.ContactSearchQuery;

/**
 * Inbound port for listing contacts with pagination.
 */
public interface ListContactsUseCase {

    /**
     * Returns a paginated contact list.
     *
     * @param query search query
     * @return page result
     */
    ContactPageResult execute(ContactSearchQuery query);
}
