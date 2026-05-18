package com.example.identity.domain.port;

import com.example.identity.domain.model.AuthUser;
import com.example.user.domain.model.User;
import com.example.user.domain.model.UserId;
import java.util.Optional;

/**
 * Outbound port for authentication-related user persistence.
 */
public interface UserAuthRepository {

    /**
     * Finds auth user by email.
     *
     * @param email email address
     * @return optional auth user
     */
    Optional<AuthUser> findByEmail(String email);

    /**
     * Finds auth user by id.
     *
     * @param id user id
     * @return optional auth user
     */
    Optional<AuthUser> findById(UserId id);

    /**
     * Saves a full user aggregate for registration.
     *
     * @param user user aggregate
     * @return saved user
     */
    User save(User user);

    /**
     * Updates password hash for a user.
     *
     * @param id user id
     * @param passwordHash new bcrypt hash
     */
    void updatePassword(UserId id, String passwordHash);

    /**
     * Checks if email exists.
     *
     * @param email email
     * @return true if exists
     */
    boolean existsByEmail(String email);

    /**
     * Updates last active timestamp for a user.
     *
     * @param id user id
     */
    void recordLastActiveAt(UserId id);
}
