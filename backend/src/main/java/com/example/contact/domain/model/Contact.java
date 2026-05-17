package com.example.contact.domain.model;

import com.example.contact.domain.valueobject.Address;
import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.domain.valueobject.Email;
import com.example.contact.domain.valueobject.PhoneNumber;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Contact aggregate root encapsulating mutable contact state and invariants.
 */
public final class Contact {

    private final ContactId id;
    private String firstName;
    private String lastName;
    private Email email;
    private PhoneNumber phone;
    private String company;
    private String jobTitle;
    private Address address;
    private String avatarUrl;
    private ContactStatus status;
    private UUID assignedToUserId;
    private final Instant createdAt;
    private Instant updatedAt;

    private Contact(
            ContactId id,
            String firstName,
            String lastName,
            Email email,
            PhoneNumber phone,
            String company,
            String jobTitle,
            Address address,
            String avatarUrl,
            ContactStatus status,
            UserId assignedToUserId,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.company = company;
        this.jobTitle = jobTitle;
        this.address = address;
        this.avatarUrl = avatarUrl;
        this.status = status;
        this.assignedToUserId = assignedToUserId;
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
     * @param company optional company
     * @param jobTitle optional job title
     * @param address optional address
     * @param status contact status
     * @param assignedToUserId optional assignee
     * @return new contact
     */
    public static Contact create(
            ContactId id,
            String firstName,
            String lastName,
            Email email,
            PhoneNumber phone,
            String company,
            String jobTitle,
            Address address,
            ContactStatus status,
            UUID assignedToUserId) {
        Instant now = Instant.now();
        return new Contact(
                id,
                requireName(firstName, "firstName"),
                requireName(lastName, "lastName"),
                email,
                phone,
                trimToNull(company),
                trimToNull(jobTitle),
                address,
                null,
                status != null ? status : ContactStatus.ACTIVE,
                assignedToUserId,
                now,
                now);
    }

    /**
     * Rehydrates a contact from persistence.
     *
     * @param id id
     * @param firstName first name
     * @param lastName last name
     * @param email email
     * @param phone phone or null
     * @param company company or null
     * @param jobTitle job title or null
     * @param address address or null
     * @param avatarUrl avatar url or null
     * @param status status
     * @param assignedToUserId assignee or null
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
            String company,
            String jobTitle,
            Address address,
            String avatarUrl,
            ContactStatus status,
            UserId assignedToUserId,
            Instant createdAt,
            Instant updatedAt) {
        return new Contact(
                id,
                firstName,
                lastName,
                email,
                phone,
                company,
                jobTitle,
                address,
                avatarUrl,
                status,
                assignedToUserId,
                createdAt,
                updatedAt);
    }

    /**
     * Updates mutable fields and refreshes {@code updatedAt}.
     *
     * @param firstName new first name
     * @param lastName new last name
     * @param email new email
     * @param phone new phone or null
     * @param company company or null
     * @param jobTitle job title or null
     * @param address address or null
     * @param status status
     * @param assignedToUserId assignee or null
     */
    public void update(
            String firstName,
            String lastName,
            Email email,
            PhoneNumber phone,
            String company,
            String jobTitle,
            Address address,
            ContactStatus status,
            UUID assignedToUserId) {
        this.firstName = requireName(firstName, "firstName");
        this.lastName = requireName(lastName, "lastName");
        this.email = Objects.requireNonNull(email, "email must not be null");
        this.phone = phone;
        this.company = trimToNull(company);
        this.jobTitle = trimToNull(jobTitle);
        this.address = address;
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.assignedToUserId = assignedToUserId;
        this.updatedAt = Instant.now();
    }

    /**
     * Updates avatar URL after upload.
     *
     * @param avatarUrl public avatar path
     */
    public void updateAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        this.updatedAt = Instant.now();
    }

    private static String requireName(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim().replaceAll("\\s+", " ");
    }

    private static String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
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

    public String company() {
        return company;
    }

    public String jobTitle() {
        return jobTitle;
    }

    public Address address() {
        return address;
    }

    public String avatarUrl() {
        return avatarUrl;
    }

    public ContactStatus status() {
        return status;
    }

    public UUID assignedToUserId() {
        return assignedToUserId;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
