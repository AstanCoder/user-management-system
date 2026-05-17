package com.example.contact.application.port.in;

import com.example.contact.application.command.TagResult;
import com.example.contact.domain.valueobject.ContactId;
import java.util.List;

/**
 * Assigns tags to a contact by tag names.
 */
public interface AssignTagsUseCase {

    /**
     * Replaces contact tags with given names.
     *
     * @param contactId contact id
     * @param tagNames tag names
     * @return assigned tags
     */
    List<TagResult> execute(ContactId contactId, List<String> tagNames);
}
