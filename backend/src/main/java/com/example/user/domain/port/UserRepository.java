package com.example.user.domain.port;

import com.example.user.domain.model.Role;
import com.example.user.domain.model.User;
import com.example.user.domain.model.UserId;
import com.example.user.domain.model.UserStatus;
import com.example.user.domain.query.PagedUsers;
import com.example.user.domain.query.UserListCriteria;
import java.time.Instant;
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
     * Returns a paginated, optionally filtered list of users.
     *
     * @param criteria page, size, and optional search term
     * @return paged users
     */
    PagedUsers findPage(UserListCriteria criteria);

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
    long countByStatus(UserStatus status);

    /**
     * Counts users with the given role.
     *
     * @param role role
     * @return count
     */
    long countByRole(Role role);

    /**
     * Counts users created on or after the given instant.
     *
     * @param since lower bound (inclusive)
     * @return count
     */
    long countCreatedSince(Instant since);
}
