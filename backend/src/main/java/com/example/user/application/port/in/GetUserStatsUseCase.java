package com.example.user.application.port.in;

import com.example.user.application.command.UserStatsResult;

/**
 * Returns aggregated user statistics.
 */
public interface GetUserStatsUseCase {

    /**
     * Computes user stats.
     *
     * @return stats result
     */
    UserStatsResult execute();
}
