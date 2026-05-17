package com.example.user.interfaces.rest.dto;

import lombok.Builder;
import lombok.Value;

/**
 * User statistics HTTP response.
 */
@Value
@Builder
public class UserStatsResponse {

    long totalUsers;
    long activeUsers;
    long invitedUsers;
    long disabledUsers;
}
