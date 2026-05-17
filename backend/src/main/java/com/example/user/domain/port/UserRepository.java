package com.example.user.domain.port;

import com.example.user.domain.model.User;
import com.example.user.domain.model.UserId;
import java.util.List;
import java.util.Optional;

/**
 * Outbound port for user persistence.
 */
public interface UserRepository {

    /**
     * Returns all users ordered by creation time descending.
     *
     * @return users
     */
    List<User> findAll();

    /**
     * Finds a user by id.
     *
     * @param id user id
     * @return optional user
     */
    Optional<User> findById(UserId id);

    /**
     * Finds a user by email (case-insensitive).
     *
     * @param email email address
     * @return optional user
     */
    Optional<User> findByEmail(String email);

    /**
     * Persists a user aggregate.
     *
     * @param user user to save
     * @return saved user
     */
    User save(User user);

    /**
     * Deletes a user by id.
     *
     * @param id user id
     */
    void delete(UserId id);

    /**
     * Counts users by status.
     *
     * @return total user count
     */
    long countAll();

    /**
     * Counts active users.
     *
     * @return active count
     */
    long countByStatus(com.example.user.domain.model.UserStatus status);
}
