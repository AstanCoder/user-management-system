package com.example.identity.infrastructure.security;

import com.example.identity.application.exception.RateLimitExceededException;
import com.example.identity.application.port.out.AuthRateLimiter;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Bucket4jAuthRateLimiter implements AuthRateLimiter {

    private final ProxyManager<String> proxyManager;
    private final long loginCapacityPerMinute;
    private final long loginFailurePenaltyCapacityPer15Minutes;
    private final long forgotPasswordCapacityPerHour;

    public Bucket4jAuthRateLimiter(
            ProxyManager<String> proxyManager,
            @Value("${app.security.rate-limit.login.capacity-per-minute:20}") long loginCapacityPerMinute,
            @Value("${app.security.rate-limit.login.failure-capacity-per-15-minutes:10}")
                    long loginFailurePenaltyCapacityPer15Minutes,
            @Value("${app.security.rate-limit.forgot-password.capacity-per-hour:8}") long forgotPasswordCapacityPerHour) {
        this.proxyManager = proxyManager;
        this.loginCapacityPerMinute = loginCapacityPerMinute;
        this.loginFailurePenaltyCapacityPer15Minutes = loginFailurePenaltyCapacityPer15Minutes;
        this.forgotPasswordCapacityPerHour = forgotPasswordCapacityPerHour;
    }

    @Override
    public void checkLoginAllowed(String ipAddress, String email) {
        consumeOrThrow(loginBucket(ipAddress), "Too many login attempts. Please try again later.");
        if (loginFailureBucket(ipAddress, email).getAvailableTokens() <= 0) {
            throw new RateLimitExceededException("Too many failed login attempts. Please wait before retrying.");
        }
    }

    @Override
    public void onLoginFailure(String ipAddress, String email) {
        consumeOrThrow(
                loginFailureBucket(ipAddress, email),
                "Too many failed login attempts. Please wait before retrying.");
    }

    @Override
    public void checkForgotPasswordAllowed(String ipAddress, String email) {
        consumeOrThrow(
                forgotPasswordBucket(ipAddress, email),
                "Too many password reset requests. Please try again later.");
    }

    private Bucket loginBucket(String ipAddress) {
        return proxyManager.getProxy(
                "auth:login:" + normalizeIp(ipAddress), this::newLoginBucketConfiguration);
    }

    private Bucket loginFailureBucket(String ipAddress, String email) {
        return proxyManager.getProxy(
                "auth:login:failure:" + normalizeIp(ipAddress) + ":" + normalizeEmail(email),
                this::newLoginFailureBucketConfiguration);
    }

    private Bucket forgotPasswordBucket(String ipAddress, String email) {
        return proxyManager.getProxy(
                "auth:forgot:" + normalizeIp(ipAddress) + ":" + normalizeEmail(email),
                this::newForgotPasswordBucketConfiguration);
    }

    private BucketConfiguration newLoginBucketConfiguration() {
        return BucketConfiguration.builder()
                .addLimit(limit -> limit.capacity(loginCapacityPerMinute)
                        .refillGreedy(loginCapacityPerMinute, Duration.ofMinutes(1)))
                .build();
    }

    private BucketConfiguration newLoginFailureBucketConfiguration() {
        return BucketConfiguration.builder()
                .addLimit(limit -> limit.capacity(loginFailurePenaltyCapacityPer15Minutes)
                        .refillGreedy(loginFailurePenaltyCapacityPer15Minutes, Duration.ofMinutes(15)))
                .build();
    }

    private BucketConfiguration newForgotPasswordBucketConfiguration() {
        return BucketConfiguration.builder()
                .addLimit(limit -> limit.capacity(forgotPasswordCapacityPerHour)
                        .refillGreedy(forgotPasswordCapacityPerHour, Duration.ofHours(1)))
                .build();
    }

    private void consumeOrThrow(Bucket bucket, String message) {
        if (!bucket.tryConsume(1)) {
            throw new RateLimitExceededException(message);
        }
    }

    private String normalizeEmail(String email) {
        return email == null ? "unknown" : email.trim().toLowerCase();
    }

    private String normalizeIp(String ipAddress) {
        return ipAddress == null || ipAddress.isBlank() ? "unknown" : ipAddress.trim();
    }
}
