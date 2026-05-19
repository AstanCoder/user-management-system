package com.example.identity.application.port.out;

public interface AuthRateLimiter {

    void checkLoginAllowed(String ipAddress, String email);

    void onLoginFailure(String ipAddress, String email);

    void checkForgotPasswordAllowed(String ipAddress, String email);
}
