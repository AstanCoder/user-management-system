package com.example.identity.interfaces.rest.mapper;

import com.example.identity.application.command.AuthResult;
import com.example.identity.application.command.CurrentUserResult;
import com.example.identity.application.command.LoginCommand;
import com.example.identity.application.command.RegisterCommand;
import com.example.identity.interfaces.rest.dto.AuthResponse;
import com.example.identity.interfaces.rest.dto.CurrentUserResponse;
import com.example.identity.interfaces.rest.dto.LoginRequest;
import com.example.identity.interfaces.rest.dto.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Maps auth REST DTOs to application commands and results.
 */
@Mapper(componentModel = "spring")
public interface AuthRestMapper {

    /**
     * Maps login request to command.
     *
     * @param request login request
     * @return login command
     */
    LoginCommand toCommand(LoginRequest request);

    /**
     * Maps register request to command.
     *
     * @param request register request
     * @return register command
     */
    RegisterCommand toCommand(RegisterRequest request);

    /**
     * Maps auth result to response.
     *
     * @param result auth result
     * @return auth response
     */
    @Mapping(target = "userId", expression = "java(result.getUserId().value().toString())")
    AuthResponse toResponse(AuthResult result);

    /**
     * Maps current user result to response.
     *
     * @param result current user result
     * @return response
     */
    @Mapping(target = "id", expression = "java(result.getId().value().toString())")
    CurrentUserResponse toResponse(CurrentUserResult result);
}
