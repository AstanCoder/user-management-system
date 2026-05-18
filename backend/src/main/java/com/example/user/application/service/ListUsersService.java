package com.example.user.application.service;

import com.example.user.application.command.UserPageResult;
import com.example.user.application.command.UserSearchQuery;
import com.example.user.application.mapper.UserApplicationMapper;
import com.example.user.application.port.in.ListUsersUseCase;
import com.example.user.domain.port.UserRepository;
import com.example.user.domain.query.PagedUsers;
import com.example.user.domain.query.UserListCriteria;

/**
 * Lists users with pagination and optional search.
 */
public final class ListUsersService implements ListUsersUseCase {

    private final UserRepository userRepository;
    private final UserApplicationMapper userApplicationMapper;

    public ListUsersService(UserRepository userRepository, UserApplicationMapper userApplicationMapper) {
        this.userRepository = userRepository;
        this.userApplicationMapper = userApplicationMapper;
    }

    @Override
    public UserPageResult execute(UserSearchQuery query) {
        PagedUsers page = userRepository.findPage(
                new UserListCriteria(query.getPage(), query.getSize(), query.getSearch()));
        return UserPageResult.builder()
                .content(page.content().stream().map(userApplicationMapper::toResult).toList())
                .totalElements(page.totalElements())
                .totalPages(page.totalPages())
                .page(page.page())
                .size(page.size())
                .build();
    }
}
