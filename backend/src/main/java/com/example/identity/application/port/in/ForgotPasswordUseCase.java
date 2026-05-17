package com.example.identity.application.port.in;

/**
 * Initiates password reset flow by email.
 */
public interface ForgotPasswordUseCase {

    /**
     * Sends reset email if user exists (always succeeds from caller perspective).
     *
     * @param email user email
     */
    void execute(String email);
}
