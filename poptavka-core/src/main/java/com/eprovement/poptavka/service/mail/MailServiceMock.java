/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.mail;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import org.springframework.mail.SimpleMailMessage;

import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

/**
 * Dummy implementation of {@link com.eprovement.poptavka.service.mail.MailService} that doesn't really send messages
 * but only remember them.
 * The sent messages can be retrieved via {@link #getSentMimeMessages()}  or {@link #getSentSimpleMailMessages()}.
 */
public class MailServiceMock implements MailService {

    private final List<MimeMessage> sentMimeMessages = new ArrayList<>();
    private final List<SimpleMailMessage> sentSimpleMailMessages = new ArrayList<>();

    @Override
    public void send(MimeMessage... message) {
        sentMimeMessages.addAll(asList(message));
    }

    @Override
    public void send(SimpleMailMessage... message) {
        sentSimpleMailMessages.addAll(asList(message));
    }

    @Override
    public void sendAsync(MimeMessage... message) {
        sentMimeMessages.addAll(asList(message));
    }

    @Override
    public void sendAsync(SimpleMailMessage... message) {
        sentSimpleMailMessages.addAll(asList(message));
    }

    @Override
    public MimeMessage createMimeMessage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<MimeMessage> getSentMimeMessages() {
        return unmodifiableList(sentMimeMessages);
    }

    public List<SimpleMailMessage> getSentSimpleMailMessages() {
        return unmodifiableList(sentSimpleMailMessages);
    }

    public void cleanAll() {
        sentMimeMessages.clear();
        sentSimpleMailMessages.clear();
    }
}
