package com.example.user.application.mapper;

import com.example.user.application.command.UserResult;
import com.example.user.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Maps domain users to application results.
 */
@Mapper(componentModel = "spring")
public interface UserApplicationMapper {

    /**
     * Maps a domain user to an application result.
     *
     * @param user domain user
     * @return application result
     */
    @Mapping(target = "id", expression = "java(user.id())")
    @Mapping(target = "email", expression = "java(user.email())")
    @Mapping(target = "firstName", expression = "java(user.firstName())")
    @Mapping(target = "lastName", expression = "java(user.lastName())")
    @Mapping(target = "role", expression = "java(user.role())")
    @Mapping(target = "status", expression = "java(user.status())")
    @Mapping(target = "createdAt", expression = "java(user.createdAt())")
    @Mapping(target = "updatedAt", expression = "java(user.updatedAt())")
    UserResult toResult(User user);
}
