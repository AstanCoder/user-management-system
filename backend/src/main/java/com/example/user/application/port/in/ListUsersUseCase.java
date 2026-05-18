package com.example.user.application.port.in;

import com.example.user.application.command.UserPageResult;
import com.example.user.application.command.UserSearchQuery;

/**
 * Lists users for administration with pagination and search.
 */
public interface ListUsersUseCase {

    /**
     * Returns a paginated list of users matching the query.
     *
     * @param query page, size, and optional search term
     * @return paginated user results
     */
    UserPageResult execute(UserSearchQuery query);
}
