package com.example.identity.domain.port;

/**
 * Outbound port for sending emails.
 */
public interface EmailSender {

    /**
     * Sends an email message.
     *
     * @param from sender address
     * @param to recipient address
     * @param subject subject line
     * @param body plain text body
     */
    void send(String from, String to, String subject, String body);
}
