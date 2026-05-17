package com.example.identity.domain.model;

import com.example.user.domain.model.Role;
import com.example.user.domain.model.UserId;
import com.example.user.domain.model.UserStatus;
import java.util.Objects;

/**
 * Authentication view of a user for login and token issuance.
 */
public final class AuthUser {

    private final UserId id;
    private final String email;
    private final String passwordHash;
    private final Role role;
    private final UserStatus status;
    private final String firstName;
    private final String lastName;

    public AuthUser(
            UserId id,
            String email,
            String passwordHash,
            Role role,
            UserStatus status,
            String firstName,
            String lastName) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.email = Objects.requireNonNull(email, "email must not be null");
        this.passwordHash = passwordHash;
        this.role = Objects.requireNonNull(role, "role must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.firstName = firstName;
        this.lastName = lastName;
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

    public Role role() {
        return role;
    }

    public UserStatus status() {
        return status;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public boolean isActive() {
        return status == UserStatus.ACTIVE;
    }
}
