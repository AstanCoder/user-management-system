package com.example.identity.domain.port;

import com.example.identity.domain.model.AuthUser;

/**
 * Outbound port for issuing JWT access tokens.
 */
public interface TokenIssuer {

    /**
     * Issues a JWT for the authenticated user.
     *
     * @param user auth user
     * @return JWT string
     */
    String issueToken(AuthUser user);
}
