package com.example.identity.domain.port;

import com.example.user.domain.model.UserId;
import java.time.Instant;
import java.util.Optional;

/**
 * Outbound port for password reset tokens.
 */
public interface PasswordResetTokenRepository {

    /**
     * Stores a reset token for a user.
     *
     * @param userId user id
     * @param token token value
     * @param expiresAt expiration instant
     */
    void save(UserId userId, String token, Instant expiresAt);

    /**
     * Finds valid unused token.
     *
     * @param token token value
     * @return user id if valid
     */
    Optional<UserId> findValidToken(String token);

    /**
     * Marks token as used.
     *
     * @param token token value
     */
    void markUsed(String token);
}
