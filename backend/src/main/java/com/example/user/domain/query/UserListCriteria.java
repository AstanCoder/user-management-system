package com.example.user.domain.query;

/**
 * Criteria for paginated user listing.
 */
public record UserListCriteria(int page, int size, String search) {

    public UserListCriteria {
        page = Math.max(page, 0);
        size = Math.max(size, 1);
        if (search != null && search.isBlank()) {
            search = null;
        }
    }
}
