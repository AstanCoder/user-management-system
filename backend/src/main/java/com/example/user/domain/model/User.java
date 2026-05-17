package com.example.user.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * User aggregate root for administration and profile data.
 */
public final class User {

    private final UserId id;
    private String email;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private Role role;
    private UserStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    private User(
            UserId id,
            String email,
            String passwordHash,
            String firstName,
            String lastName,
            Role role,
            UserStatus status,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Creates a new active user.
     *
     * @param id user id
     * @param email email address
     * @param passwordHash bcrypt hash
     * @param firstName first name
     * @param lastName last name
     * @param role role
     * @return new user
     */
    public static User create(
            UserId id,
            String email,
            String passwordHash,
            String firstName,
            String lastName,
            Role role) {
        Instant now = Instant.now();
        return new User(
                id,
                requireEmail(email),
                passwordHash,
                requireName(firstName, "firstName"),
                requireName(lastName, "lastName"),
                Objects.requireNonNull(role, "role must not be null"),
                UserStatus.ACTIVE,
                now,
                now);
    }

    /**
     * Creates an invited user without a password yet.
     *
     * @param id user id
     * @param email email address
     * @param firstName first name
     * @param lastName last name
     * @param role role
     * @return invited user
     */
    public static User createInvited(UserId id, String email, String firstName, String lastName, Role role) {
        Instant now = Instant.now();
        return new User(
                id,
                requireEmail(email),
                null,
                requireName(firstName, "firstName"),
                requireName(lastName, "lastName"),
                Objects.requireNonNull(role, "role must not be null"),
                UserStatus.INVITED,
                now,
                now);
    }

    /**
     * Rehydrates a user from persistence.
     *
     * @param id id
     * @param email email
     * @param passwordHash password hash or null
     * @param firstName first name
     * @param lastName last name
     * @param role role
     * @param status status
     * @param createdAt created at
     * @param updatedAt updated at
     * @return user
     */
    public static User restore(
            UserId id,
            String email,
            String passwordHash,
            String firstName,
            String lastName,
            Role role,
            UserStatus status,
            Instant createdAt,
            Instant updatedAt) {
        return new User(id, email, passwordHash, firstName, lastName, role, status, createdAt, updatedAt);
    }

    /**
     * Updates profile fields and role.
     *
     * @param firstName first name
     * @param lastName last name
     * @param role role
     * @param status status
     */
    public void update(String firstName, String lastName, Role role, UserStatus status) {
        this.firstName = requireName(firstName, "firstName");
        this.lastName = requireName(lastName, "lastName");
        this.role = Objects.requireNonNull(role, "role must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.updatedAt = Instant.now();
    }

    /**
     * Sets password hash after invite acceptance or reset.
     *
     * @param passwordHash bcrypt hash
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash must not be null");
        if (this.status == UserStatus.INVITED) {
            this.status = UserStatus.ACTIVE;
        }
        this.updatedAt = Instant.now();
    }

    private static String requireEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email must not be blank");
        }
        return email.trim().toLowerCase();
    }

    private static String requireName(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }

    public UserId id() {
        return id;
    }

    public String email() {
        return email;
    }

    public String passwordHash() {
        return passwordHash;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public Role role() {
        return role;
    }

    public UserStatus status() {
        return status;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
