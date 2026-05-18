package com.example.user.application.port.in;

import com.example.user.domain.model.UserId;

/**
 * Resends invitation email for users still in invited status.
 */
public interface ResendInvitationUseCase {

    /**
     * Generates a new invitation token and sends invitation again.
     *
     * @param id invited user id
     */
    void execute(UserId id);
}
