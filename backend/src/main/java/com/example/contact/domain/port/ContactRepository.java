package com.example.contact.domain.port;

import com.example.contact.domain.model.Contact;
import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.domain.valueobject.Email;
import java.util.List;
import java.util.Optional;

/**
 * Outbound port for contact persistence operations using domain types only.
 */
public interface ContactRepository {

    /**
     * Returns all contacts.
     *
     * @return contact list
     */
    List<Contact> findAll();

    /**
     * Finds a contact by id.
     *
     * @param id contact id
     * @return optional contact
     */
    Optional<Contact> findById(ContactId id);

    /**
     * Finds a contact by email.
     *
     * @param email email value object
     * @return optional contact
     */
    Optional<Contact> findByEmail(Email email);

    /**
     * Persists the contact aggregate.
     *
     * @param contact contact to save
     * @return saved contact
     */
    Contact save(Contact contact);

    /**
     * Deletes a contact by id.
     *
     * @param id contact id
     */
    void delete(ContactId id);
}
