/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.service.mail;

import com.google.common.base.Preconditions;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;

/**
 * Main implementation of {@link MailService}. For mail settings see mail.properties.
 */
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender javaMailSender;


    @Override
    public void send(MimeMessage... message) {
        Preconditions.checkNotNull(javaMailSender);
        this.javaMailSender.send(message);
    }

    @Override
    public void send(SimpleMailMessage... message) {
        this.javaMailSender.send(message);
    }


    @Async
    @Override
    public void sendAsync(MimeMessage... message) {
        Preconditions.checkNotNull(javaMailSender);
        this.javaMailSender.send(message);
    }

    @Async
    @Override
    public void sendAsync(SimpleMailMessage... message) {
        this.javaMailSender.send(message);
    }
}
