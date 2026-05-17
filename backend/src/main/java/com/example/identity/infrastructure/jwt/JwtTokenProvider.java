package com.example.identity.infrastructure.jwt;

import com.example.identity.domain.model.AuthUser;
import com.example.identity.domain.port.TokenIssuer;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Issues JWT access tokens using jjwt.
 */
@Component
public class JwtTokenProvider implements TokenIssuer {

    private final SecretKey secretKey;
    private final long expirationHours;

    public JwtTokenProvider(
            @Value("${app.jwt.secret:nexus-crm-dev-secret-key-min-32-chars!!}") String secret,
            @Value("${app.jwt.expiration-hours:24}") long expirationHours) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationHours = expirationHours;
    }

    @Override
    public String issueToken(AuthUser user) {
        Instant now = Instant.now();
        Instant expiry = now.plus(expirationHours, ChronoUnit.HOURS);
        return Jwts.builder()
                .subject(user.id().value().toString())
                .claim("email", user.email())
                .claim("role", user.role().name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Parses user id from a JWT subject claim.
     *
     * @param token jwt string
     * @return subject user id string
     */
    public String parseSubject(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
