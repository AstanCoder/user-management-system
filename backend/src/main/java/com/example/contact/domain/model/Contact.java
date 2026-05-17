package com.example.contact.domain.model;

import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.domain.valueobject.Email;
import com.example.contact.domain.valueobject.PhoneNumber;
import java.time.Instant;
import java.util.Objects;

/**
 * Contact aggregate root encapsulating mutable contact state and invariants.
 */
public final class Contact {

    private final ContactId id;
    private String firstName;
    private String lastName;
    private Email email;
    private PhoneNumber phone;
    private final Instant createdAt;
    private Instant updatedAt;

    private Contact(
            ContactId id,
            String firstName,
            String lastName,
            Email email,
            PhoneNumber phone,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Creates a new contact aggregate with generated timestamps.
     *
     * @param id contact id
     * @param firstName first name
     * @param lastName last name
     * @param email email value object
     * @param phone optional phone
     * @return new contact
     */
    public static Contact create(
            ContactId id, String firstName, String lastName, Email email, PhoneNumber phone) {
        Instant now = Instant.now();
        return new Contact(id, requireName(firstName, "firstName"), requireName(lastName, "lastName"),
                email, phone, now, now);
    }

    /**
     * Rehydrates a contact from persistence.
     *
     * @param id id
     * @param firstName first name
     * @param lastName last name
     * @param email email
     * @param phone phone or null
     * @param createdAt created timestamp
     * @param updatedAt updated timestamp
     * @return contact instance
     */
    public static Contact restore(
            ContactId id,
            String firstName,
            String lastName,
            Email email,
            PhoneNumber phone,
            Instant createdAt,
            Instant updatedAt) {
        return new Contact(id, firstName, lastName, email, phone, createdAt, updatedAt);
    }

    /**
     * Updates mutable fields and refreshes {@code updatedAt}.
     *
     * @param firstName new first name
     * @param lastName new last name
     * @param email new email
     * @param phone new phone or null
     */
    public void update(String firstName, String lastName, Email email, PhoneNumber phone) {
        this.firstName = requireName(firstName, "firstName");
        this.lastName = requireName(lastName, "lastName");
        this.email = Objects.requireNonNull(email, "email must not be null");
        this.phone = phone;
        this.updatedAt = Instant.now();
    }

    private static String requireName(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim().replaceAll("\\s+", " ");
    }

    public ContactId id() {
        return id;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public Email email() {
        return email;
    }

    public PhoneNumber phone() {
        return phone;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
