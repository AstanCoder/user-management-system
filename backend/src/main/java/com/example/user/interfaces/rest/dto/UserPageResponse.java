package com.example.user.interfaces.rest.dto;

import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 * Paginated user list HTTP response.
 */
@Value
@Builder
public class UserPageResponse {

    List<UserResponse> content;
    long totalElements;
    int totalPages;
    int page;
    int size;
}
