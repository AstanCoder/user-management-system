package com.example.user.application.service;

import com.example.user.application.command.UserStatsResult;
import com.example.user.application.port.in.GetUserStatsUseCase;
import com.example.user.domain.model.UserStatus;
import com.example.user.domain.port.UserRepository;

/**
 * Aggregates user counts by status.
 */
public final class GetUserStatsService implements GetUserStatsUseCase {

    private final UserRepository userRepository;

    public GetUserStatsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserStatsResult execute() {
        long total = userRepository.countAll();
        long active = userRepository.countByStatus(UserStatus.ACTIVE);
        long invited = userRepository.countByStatus(UserStatus.INVITED);
        long disabled = userRepository.countByStatus(UserStatus.DISABLED);
        return UserStatsResult.builder()
                .totalUsers(total)
                .activeUsers(active)
                .invitedUsers(invited)
                .disabledUsers(disabled)
                .build();
    }
}
