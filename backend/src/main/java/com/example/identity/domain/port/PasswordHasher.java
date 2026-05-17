package com.example.identity.domain.port;

/**
 * Outbound port for password hashing and verification.
 */
public interface PasswordHasher {

    /**
     * Hashes a plain password.
     *
     * @param plainPassword plain password
     * @return bcrypt hash
     */
    String hash(String plainPassword);

    /**
     * Verifies plain password against hash.
     *
     * @param plainPassword plain password
     * @param passwordHash stored hash
     * @return true if matches
     */
    boolean matches(String plainPassword, String passwordHash);
}
