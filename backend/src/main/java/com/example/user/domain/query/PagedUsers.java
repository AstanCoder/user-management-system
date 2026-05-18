package com.example.user.domain.query;

import com.example.user.domain.model.User;
import java.util.List;

/**
 * Paginated user list from the repository.
 */
public record PagedUsers(List<User> content, long totalElements, int totalPages, int page, int size) {}
