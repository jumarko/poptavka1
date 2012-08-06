/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.mail;

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
 *     This class is used in {@link com.eprovement.poptavka.application.logging.ExceptionLogger}
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
            LOGGER.info("action=send_mails status=start messages=" + message);
            this.javaMailSender.send(message);
            LOGGER.info("action=send_mails status=finish messages=" + message);
        } catch (Exception e) {
            LOGGER.error("action=send_mails status=error messages=" + message, e);
        }
    }

    @Override
    public void send(SimpleMailMessage... message) {
        try {
            Preconditions.checkNotNull(javaMailSender);
            LOGGER.info("action=send_mail status=start messages=" + message);
            this.javaMailSender.send(message);
            LOGGER.info("action=send_mail status=finish messages=" + message);
        } catch (Exception e) {
            LOGGER.error("action=send_mail status=error messages=" + message, e);
        }
    }


    @Async
    @Override
    public void sendAsync(MimeMessage... message) {
        try {
            Preconditions.checkNotNull(javaMailSender);
            LOGGER.info("action=send_mail_async status=start messages=" + message);
            this.javaMailSender.send(message);
            LOGGER.info("action=send_mail_async status=finish messages=" + message);
        } catch (Exception e) {
            LOGGER.info("action=send_mail_async status=error messages=" + message, e);
        }
    }

    @Async
    @Override
    public void sendAsync(SimpleMailMessage... message) {
        try {
            Preconditions.checkNotNull(javaMailSender);
            LOGGER.info("action=send_mail_async status=start messages=" + message);
            this.javaMailSender.send(message);
            LOGGER.info("action=send_mail_async status=finish messages=" + message);
        } catch (Exception e) {
            LOGGER.error("action=send_mail_async status=error messages=" + message);
        }
    }
}
