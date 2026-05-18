package com.example.user.application.command;

import lombok.Builder;
import lombok.Value;

/**
 * Query parameters for paginated user listing.
 */
@Value
@Builder
public class UserSearchQuery {

    int page;
    int size;
    String search;

    public static UserSearchQuery of(int page, int size, String search) {
        return UserSearchQuery.builder()
                .page(Math.max(page, 0))
                .size(Math.max(size, 1))
                .search(search == null || search.isBlank() ? null : search.trim())
                .build();
    }
}
