package com.example.user.application.service;

import com.example.identity.domain.port.InvitationTokenRepository;
import com.example.user.application.port.in.ResendInvitationUseCase;
import com.example.user.domain.exception.UserInvitationResendNotAllowedException;
import com.example.user.domain.exception.UserNotFoundException;
import com.example.user.domain.model.UserId;
import com.example.user.domain.model.UserStatus;
import com.example.user.domain.port.UserInvitationEmailSender;
import com.example.user.domain.port.UserRepository;
import java.util.UUID;

/**
 * Resends invitation for users that are still invited.
 */
public final class ResendInvitationService implements ResendInvitationUseCase {

    private final UserRepository userRepository;
    private final InvitationTokenRepository invitationTokenRepository;
    private final UserInvitationEmailSender invitationEmailSender;

    public ResendInvitationService(
            UserRepository userRepository,
            InvitationTokenRepository invitationTokenRepository,
            UserInvitationEmailSender invitationEmailSender) {
        this.userRepository = userRepository;
        this.invitationTokenRepository = invitationTokenRepository;
        this.invitationEmailSender = invitationEmailSender;
    }

    @Override
    public void execute(UserId id) {
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        if (user.status() != UserStatus.INVITED) {
            throw new UserInvitationResendNotAllowedException(id);
        }
        String token = UUID.randomUUID().toString();
        invitationTokenRepository.save(user.id(), token);
        invitationEmailSender.sendInvitation(user.email(), user.firstName(), token);
    }
}
