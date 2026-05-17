package com.example.user.application.port.in;

import com.example.user.application.command.UserResult;
import java.util.List;

/**
 * Lists all users for administration.
 */
public interface ListUsersUseCase {

    /**
     * Returns all users.
     *
     * @return user results
     */
    List<UserResult> execute();
}
