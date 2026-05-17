package com.example.user.application.service;

import com.example.user.application.command.UserResult;
import com.example.user.application.mapper.UserApplicationMapper;
import com.example.user.application.port.in.ListUsersUseCase;
import com.example.user.domain.port.UserRepository;
import java.util.Comparator;
import java.util.List;

/**
 * Lists all users from the repository.
 */
public final class ListUsersService implements ListUsersUseCase {

    private final UserRepository userRepository;
    private final UserApplicationMapper userApplicationMapper;

    public ListUsersService(UserRepository userRepository, UserApplicationMapper userApplicationMapper) {
        this.userRepository = userRepository;
        this.userApplicationMapper = userApplicationMapper;
    }

    @Override
    public List<UserResult> execute() {
        return userRepository.findAll().stream()
                .sorted(Comparator.comparing(com.example.user.domain.model.User::createdAt).reversed())
                .map(userApplicationMapper::toResult)
                .toList();
    }
}
