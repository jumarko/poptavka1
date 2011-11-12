/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.service.mail;

import com.google.common.base.Preconditions;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;

/**
 * Main implementation of {@link MailService}. For mail settings see mail.properties.
 * <p>
 *     This class is used in {@link cz.poptavka.sample.application.logging.ExceptionLogger}
 *     therefore it MUST NOT throw an exception because infinite cycles can occur:
 *     exception logger tries to send mail - mail sender throws an exception - exception logger catches the exception
 *     and try to send mail - mail sender throws an exception again ...
 * </p>
 */
public class MailServiceImpl implements MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    private JavaMailSender javaMailSender;

    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void send(MimeMessage... message) {
        try {
            Preconditions.checkNotNull(javaMailSender);
            this.javaMailSender.send(message);
        } catch (Exception e) {
            LOGGER.error("An exception occured while trying to send an email", e);
        }
    }

    @Override
    public void send(SimpleMailMessage... message) {
        try {
            Preconditions.checkNotNull(javaMailSender);
            this.javaMailSender.send(message);
        } catch (Exception e) {
            LOGGER.error("An exception occured while trying to send an email", e);
        }
    }


    @Async
    @Override
    public void sendAsync(MimeMessage... message) {
        try {
            Preconditions.checkNotNull(javaMailSender);
            this.javaMailSender.send(message);
        } catch (Exception e) {
            LOGGER.error("An exception occured while trying to send an email", e);
        }
    }

    @Async
    @Override
    public void sendAsync(SimpleMailMessage... message) {
        try {
            Preconditions.checkNotNull(javaMailSender);
            this.javaMailSender.send(message);
        } catch (Exception e) {
            LOGGER.error("An exception occured while trying to send an email", e);
        }
    }
}
