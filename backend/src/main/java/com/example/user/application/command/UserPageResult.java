package com.example.user.application.command;

import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 * Paginated user list result.
 */
@Value
@Builder
public class UserPageResult {

    List<UserResult> content;
    long totalElements;
    int totalPages;
    int page;
    int size;
}
