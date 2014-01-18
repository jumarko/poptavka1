/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.mail;

import org.springframework.mail.SimpleMailMessage;

import javax.mail.internet.MimeMessage;

/**
 * Dummy implementation of {@link com.eprovement.poptavka.service.mail.MailService} that doesn't do anything.
 */
public class MailServiceMock implements MailService {

    @Override
    public void send(MimeMessage... message) { }

    @Override
    public void send(SimpleMailMessage... message) { }

    @Override
    public void sendAsync(MimeMessage... message) { }

    @Override
    public void sendAsync(SimpleMailMessage... message) { }
}
