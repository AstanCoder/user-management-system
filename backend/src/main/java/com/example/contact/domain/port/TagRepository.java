package com.example.contact.domain.port;

import com.example.contact.domain.model.Tag;
import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.domain.valueobject.TagId;
import java.util.List;
import java.util.Optional;

/**
 * Outbound port for tag persistence.
 */
public interface TagRepository {

    /**
     * Lists tags assigned to a contact.
     *
     * @param contactId contact id
     * @return tags
     */
    List<Tag> findByContactId(ContactId contactId);

    /**
     * Finds tag by name.
     *
     * @param name tag name
     * @return optional tag
     */
    Optional<Tag> findByName(String name);

    /**
     * Saves tag.
     *
     * @param tag tag
     * @return saved tag
     */
    Tag save(Tag tag);

    /**
     * Assigns tag to contact.
     *
     * @param contactId contact id
     * @param tagId tag id
     */
    void assignToContact(ContactId contactId, TagId tagId);

    /**
     * Removes all tags from contact before reassignment.
     *
     * @param contactId contact id
     */
    void clearContactTags(ContactId contactId);
}
