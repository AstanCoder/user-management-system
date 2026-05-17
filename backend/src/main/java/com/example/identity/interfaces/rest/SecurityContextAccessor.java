package com.example.identity.interfaces.rest;

import com.example.identity.infrastructure.security.AuthenticatedUserPrincipal;
import com.example.user.domain.model.UserId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Reads authenticated user id from Spring Security context.
 */
public final class SecurityContextAccessor {

    private SecurityContextAccessor() {}

    /**
     * Returns the authenticated user id.
     *
     * @return user id
     */
    public static UserId currentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUserPrincipal principal)) {
            throw new IllegalStateException("No authenticated user in context");
        }
        return principal.userId();
    }
}
