package com.example.user.application.service;

import com.example.identity.domain.port.InvitationTokenRepository;
import com.example.user.application.command.InviteUserCommand;
import com.example.user.application.command.UserResult;
import com.example.user.application.mapper.UserApplicationMapper;
import com.example.user.application.port.in.InviteUserUseCase;
import com.example.user.domain.exception.DuplicateUserEmailException;
import com.example.user.domain.model.User;
import com.example.user.domain.model.UserId;
import com.example.user.domain.port.UserInvitationEmailSender;
import com.example.user.domain.port.UserRepository;
import java.util.UUID;

/**
 * Invites a user and sends invitation email.
 */
public final class InviteUserService implements InviteUserUseCase {

    private final UserRepository userRepository;
    private final InvitationTokenRepository invitationTokenRepository;
    private final UserInvitationEmailSender invitationEmailSender;
    private final UserApplicationMapper userApplicationMapper;

    public InviteUserService(
            UserRepository userRepository,
            InvitationTokenRepository invitationTokenRepository,
            UserInvitationEmailSender invitationEmailSender,
            UserApplicationMapper userApplicationMapper) {
        this.userRepository = userRepository;
        this.invitationTokenRepository = invitationTokenRepository;
        this.invitationEmailSender = invitationEmailSender;
        this.userApplicationMapper = userApplicationMapper;
    }

    @Override
    public UserResult execute(InviteUserCommand command) {
        userRepository
                .findByEmail(command.getEmail())
                .ifPresent(u -> {
                    throw new DuplicateUserEmailException(command.getEmail());
                });
        String token = UUID.randomUUID().toString();
        User user = User.createInvited(
                UserId.generate(),
                command.getEmail(),
                command.getFirstName(),
                command.getLastName(),
                command.getRole());
        User saved = userRepository.save(user);
        invitationTokenRepository.save(saved.id(), token);
        invitationEmailSender.sendInvitation(saved.email(), saved.firstName(), token);
        return userApplicationMapper.toResult(saved);
    }
}
