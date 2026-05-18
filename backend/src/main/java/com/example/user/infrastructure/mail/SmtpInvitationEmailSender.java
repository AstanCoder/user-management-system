package com.example.user.infrastructure.mail;

import com.example.identity.domain.port.EmailSender;
import com.example.user.domain.port.UserInvitationEmailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Sends user invitations via the identity email sender port.
 */
@Component
public class SmtpInvitationEmailSender implements UserInvitationEmailSender {

    private final EmailSender emailSender;
    private final String mailFrom;
    private final String frontendBaseUrl;

    public SmtpInvitationEmailSender(
            EmailSender emailSender,
            @Value("${app.mail.from:noreply@nexuscrm.com}") String mailFrom,
            @Value("${app.frontend.base-url:http://localhost:3000}") String frontendBaseUrl) {
        this.emailSender = emailSender;
        this.mailFrom = mailFrom;
        this.frontendBaseUrl = frontendBaseUrl;
    }

    @Override
    public void sendInvitation(String toEmail, String firstName, String inviteToken) {
        String inviteLink = frontendBaseUrl + "/accept-invitation?token=" + inviteToken;
        String subject = "You are invited to Nexus CRM";
        String body = "Hello " + firstName + ",\n\n"
                + "You have been invited to join Nexus CRM.\n"
                + "Complete your registration using this link: " + inviteLink + "\n\n"
                + "— Nexus CRM Team";
        emailSender.send(mailFrom, toEmail, subject, body);
    }
}
