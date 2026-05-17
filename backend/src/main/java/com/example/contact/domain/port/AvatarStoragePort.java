package com.example.contact.domain.port;

import com.example.contact.domain.valueobject.ContactId;

/**
 * Outbound port for storing contact avatar files.
 */
public interface AvatarStoragePort {

    /**
     * Stores avatar bytes and returns a public URL path.
     *
     * @param contactId contact id
     * @param contentType mime type
     * @param data file bytes
     * @return avatar URL path
     */
    String store(ContactId contactId, String contentType, byte[] data);
}
