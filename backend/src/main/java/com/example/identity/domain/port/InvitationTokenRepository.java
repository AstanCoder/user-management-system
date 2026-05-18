package com.example.identity.domain.port;

import com.example.user.domain.model.UserId;
import java.util.Optional;

/**
 * Outbound port for invitation completion tokens.
 */
public interface InvitationTokenRepository {

    /**
     * Stores a token for an invited user.
     *
     * @param userId invited user id
     * @param token token value
     */
    void save(UserId userId, String token);

    /**
     * Finds a valid unused invitation token.
     *
     * @param token token value
     * @return invited user id when token is valid and unused
     */
    Optional<UserId> findValidToken(String token);

    /**
     * Marks token as consumed.
     *
     * @param token token value
     */
    void markUsed(String token);
}
