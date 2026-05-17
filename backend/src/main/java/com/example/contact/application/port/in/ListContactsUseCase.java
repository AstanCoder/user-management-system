package com.example.contact.application.port.in;

import com.example.contact.application.command.ContactResult;
import java.util.List;

/**
 * Inbound port for listing all contacts.
 */
public interface ListContactsUseCase {

    /**
     * Returns all contacts ordered by creation time descending.
     *
     * @return contact results
     */
    List<ContactResult> execute();
}
