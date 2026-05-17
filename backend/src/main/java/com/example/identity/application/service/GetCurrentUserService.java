package com.example.identity.application.service;

import com.example.identity.application.command.CurrentUserResult;
import com.example.identity.application.port.in.GetCurrentUserUseCase;
import com.example.identity.domain.exception.AuthUserNotFoundException;
import com.example.identity.domain.model.AuthUser;
import com.example.identity.domain.port.UserAuthRepository;
import com.example.user.domain.model.UserId;

/**
 * Loads current user profile for /me endpoint.
 */
public final class GetCurrentUserService implements GetCurrentUserUseCase {

    private final UserAuthRepository userAuthRepository;

    public GetCurrentUserService(UserAuthRepository userAuthRepository) {
        this.userAuthRepository = userAuthRepository;
    }

    @Override
    public CurrentUserResult execute(UserId userId) {
        AuthUser user = userAuthRepository.findById(userId).orElseThrow(() -> new AuthUserNotFoundException(userId));
        return CurrentUserResult.builder()
                .id(user.id())
                .email(user.email())
                .firstName(user.firstName())
                .lastName(user.lastName())
                .role(user.role())
                .status(user.status())
                .build();
    }
}
