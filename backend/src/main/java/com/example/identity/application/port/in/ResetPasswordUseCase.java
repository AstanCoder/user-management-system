package com.example.identity.application.port.in;

/**
 * Resets password using a valid token.
 */
public interface ResetPasswordUseCase {

    /**
     * Resets password.
     *
     * @param token reset token
     * @param newPassword new plain password
     */
    void execute(String token, String newPassword);
}
