package com.example.user.interfaces.rest;

import com.example.user.application.command.UserPageResult;
import com.example.user.application.command.UserResult;
import com.example.user.application.command.UserSearchQuery;
import com.example.user.application.command.UserStatsResult;
import com.example.user.application.port.in.CreateUserUseCase;
import com.example.user.application.port.in.DeleteUserUseCase;
import com.example.user.application.port.in.GetUserStatsUseCase;
import com.example.user.application.port.in.InviteUserUseCase;
import com.example.user.application.port.in.ListUsersUseCase;
import com.example.user.application.port.in.UpdateUserUseCase;
import com.example.user.domain.model.UserId;
import com.example.user.interfaces.rest.dto.CreateUserRequest;
import com.example.user.interfaces.rest.dto.InviteUserRequest;
import com.example.user.interfaces.rest.dto.UpdateUserRequest;
import com.example.user.interfaces.rest.dto.UserPageResponse;
import com.example.user.interfaces.rest.dto.UserResponse;
import com.example.user.interfaces.rest.dto.UserStatsResponse;
import com.example.user.interfaces.rest.mapper.UserRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin-only REST adapter for user management.
 */
@Tag(name = "Users", description = "User administration (ADMIN only)")
@RestController
@RequestMapping("/api/users")
@Validated
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final ListUsersUseCase listUsersUseCase;
    private final GetUserStatsUseCase getUserStatsUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final InviteUserUseCase inviteUserUseCase;
    private final UserRestMapper mapper;

    public UserController(
            ListUsersUseCase listUsersUseCase,
            GetUserStatsUseCase getUserStatsUseCase,
            CreateUserUseCase createUserUseCase,
            UpdateUserUseCase updateUserUseCase,
            DeleteUserUseCase deleteUserUseCase,
            InviteUserUseCase inviteUserUseCase,
            UserRestMapper mapper) {
        this.listUsersUseCase = listUsersUseCase;
        this.getUserStatsUseCase = getUserStatsUseCase;
        this.createUserUseCase = createUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.inviteUserUseCase = inviteUserUseCase;
        this.mapper = mapper;
    }

    /**
     * Lists all users.
     *
     * @return user list
     */
    @Operation(summary = "List users")
    @GetMapping
    public UserPageResponse list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search) {
        UserPageResult result = listUsersUseCase.execute(UserSearchQuery.of(page, size, search));
        return mapper.toPageResponse(result);
    }

    /**
     * Returns user statistics.
     *
     * @return stats
     */
    @Operation(summary = "User stats")
    @GetMapping("/stats")
    public UserStatsResponse stats() {
        UserStatsResult result = getUserStatsUseCase.execute();
        return mapper.toResponse(result);
    }

    /**
     * Creates a user.
     *
     * @param request create request
     * @return created user
     */
    @Operation(summary = "Create user")
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        UserResult result = createUserUseCase.execute(mapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    /**
     * Invites a user.
     *
     * @param request invite request
     * @return invited user
     */
    @Operation(summary = "Invite user")
    @PostMapping("/invite")
    public ResponseEntity<UserResponse> invite(@Valid @RequestBody InviteUserRequest request) {
        UserResult result = inviteUserUseCase.execute(mapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    /**
     * Updates a user.
     *
     * @param id user id
     * @param request update request
     * @return updated user
     */
    @Operation(summary = "Update user")
    @PutMapping("/{id}")
    public UserResponse update(@PathVariable String id, @Valid @RequestBody UpdateUserRequest request) {
        UserResult result = updateUserUseCase.execute(mapper.toCommand(id, request));
        return mapper.toResponse(result);
    }

    /**
     * Deletes a user.
     *
     * @param id user id
     * @return no content
     */
    @Operation(summary = "Delete user")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        deleteUserUseCase.execute(UserId.of(id));
        return ResponseEntity.noContent().build();
    }
}
