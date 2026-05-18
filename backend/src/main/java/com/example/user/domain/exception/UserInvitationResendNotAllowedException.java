package com.example.user.domain.exception;

import com.example.user.domain.model.UserId;

/**
 * Thrown when invitation cannot be resent due to user status.
 */
public class UserInvitationResendNotAllowedException extends RuntimeException {

    public UserInvitationResendNotAllowedException(UserId id) {
        super("Invitation can only be resent for invited users: " + id.value());
    }
}
