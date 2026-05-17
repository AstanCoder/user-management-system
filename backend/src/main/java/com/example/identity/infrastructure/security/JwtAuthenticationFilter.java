package com.example.identity.infrastructure.security;

import com.example.identity.domain.model.AuthUser;
import com.example.identity.domain.port.UserAuthRepository;
import com.example.identity.infrastructure.jwt.JwtTokenProvider;
import com.example.user.domain.model.Role;
import com.example.user.domain.model.UserId;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT bearer token authentication filter.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserAuthRepository userAuthRepository;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserAuthRepository userAuthRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userAuthRepository = userAuthRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                String userId = jwtTokenProvider.parseSubject(token);
                Optional<AuthUser> user = userAuthRepository.findById(UserId.of(userId));
                user.filter(AuthUser::isActive).ifPresent(authUser -> setAuthentication(request, authUser));
            } catch (Exception ignored) {
            }
        }
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(HttpServletRequest request, AuthUser user) {
        AuthenticatedUserPrincipal principal =
                new AuthenticatedUserPrincipal(user.id(), user.email(), user.role());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
