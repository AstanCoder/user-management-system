package com.example.user.application.command;

import lombok.Builder;
import lombok.Value;

/**
 * Aggregated user statistics for admin dashboard.
 */
@Value
@Builder
public class UserStatsResult {

    long totalUsers;
    long activeUsers;
    long invitedUsers;
    long disabledUsers;
    long adminCount;
    long editorCount;
    long viewerCount;
    long invitedPendingCount;
    long usersCreatedLast7Days;
}
