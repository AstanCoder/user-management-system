package com.example.user.application.service;

import com.example.user.application.command.UserStatsResult;
import com.example.user.application.port.in.GetUserStatsUseCase;
import com.example.user.domain.model.Role;
import com.example.user.domain.model.UserStatus;
import com.example.user.domain.port.UserRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Aggregates user counts by status and role.
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
        long adminCount = userRepository.countByRole(Role.ADMIN);
        long editorCount = userRepository.countByRole(Role.EDITOR);
        long viewerCount = userRepository.countByRole(Role.VIEWER);
        Instant sevenDaysAgo = Instant.now().minus(7, ChronoUnit.DAYS);
        long usersCreatedLast7Days = userRepository.countCreatedSince(sevenDaysAgo);

        return UserStatsResult.builder()
                .totalUsers(total)
                .activeUsers(active)
                .invitedUsers(invited)
                .disabledUsers(disabled)
                .adminCount(adminCount)
                .editorCount(editorCount)
                .viewerCount(viewerCount)
                .invitedPendingCount(invited)
                .usersCreatedLast7Days(usersCreatedLast7Days)
                .build();
    }
}
