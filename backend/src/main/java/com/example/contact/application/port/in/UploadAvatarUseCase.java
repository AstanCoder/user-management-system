package com.example.contact.application.port.in;

import com.example.contact.application.command.ContactResult;
import com.example.contact.domain.valueobject.ContactId;

/**
 * Uploads avatar for a contact.
 */
public interface UploadAvatarUseCase {

    /**
     * Stores avatar and updates contact.
     *
     * @param contactId contact id
     * @param contentType mime type
     * @param data file bytes
     * @return updated contact
     */
    ContactResult execute(ContactId contactId, String contentType, byte[] data);
}
