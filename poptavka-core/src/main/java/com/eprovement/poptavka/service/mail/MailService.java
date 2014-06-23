/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.mail;

import javax.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;

public interface MailService {

    /**
     * Sends given mail message. Execution of this method blocks.
     * @param message
     */
    void send(MimeMessage... message);

    /**
     * Sends given mail message. Execution of this method blocks.
     * @param message
     */
    void send(SimpleMailMessage... message);

    /**
     * Sends given mail message asynchronously.
     * @param message
     */
    void sendAsync(MimeMessage... message);

    /**
     * Sends given mail message asynchronously.
     * @param message
     */
    void sendAsync(SimpleMailMessage... message);

    /**
     * @return MIME message.
     */
    MimeMessage createMimeMessage();
}
