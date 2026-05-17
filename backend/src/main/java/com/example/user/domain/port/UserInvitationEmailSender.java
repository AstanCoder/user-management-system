package com.example.user.domain.port;

/**
 * Outbound port for sending user invitation emails.
 */
public interface UserInvitationEmailSender {

    /**
     * Sends an invitation email with a temporary link or instructions.
     *
     * @param toEmail recipient email
     * @param firstName recipient first name
     * @param inviteToken optional token for accept flow
     */
    void sendInvitation(String toEmail, String firstName, String inviteToken);
}
