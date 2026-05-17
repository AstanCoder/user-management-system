package com.example.user.interfaces.rest.mapper;

import com.example.user.application.command.CreateUserCommand;
import com.example.user.application.command.InviteUserCommand;
import com.example.user.application.command.UpdateUserCommand;
import com.example.user.application.command.UserResult;
import com.example.user.application.command.UserStatsResult;
import com.example.user.domain.model.UserId;
import com.example.user.interfaces.rest.dto.CreateUserRequest;
import com.example.user.interfaces.rest.dto.InviteUserRequest;
import com.example.user.interfaces.rest.dto.UpdateUserRequest;
import com.example.user.interfaces.rest.dto.UserResponse;
import com.example.user.interfaces.rest.dto.UserStatsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Maps user REST DTOs to application layer.
 */
@Mapper(componentModel = "spring", imports = UserId.class)
public interface UserRestMapper {

    /**
     * Maps create request to command.
     *
     * @param request create request
     * @return command
     */
    CreateUserCommand toCommand(CreateUserRequest request);

    /**
     * Maps invite request to command.
     *
     * @param request invite request
     * @return command
     */
    InviteUserCommand toCommand(InviteUserRequest request);

    /**
     * Maps update request to command.
     *
     * @param id user id path
     * @param request update request
     * @return command
     */
    @Mapping(target = "id", expression = "java(UserId.of(id))")
    UpdateUserCommand toCommand(String id, UpdateUserRequest request);

    /**
     * Maps user result to response.
     *
     * @param result user result
     * @return response
     */
    @Mapping(target = "id", expression = "java(result.getId().value().toString())")
    UserResponse toResponse(UserResult result);

    /**
     * Maps stats result to response.
     *
     * @param result stats result
     * @return response
     */
    UserStatsResponse toResponse(UserStatsResult result);
}
