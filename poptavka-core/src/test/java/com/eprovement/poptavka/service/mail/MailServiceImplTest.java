/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.mail;

import com.eprovement.poptavka.base.integration.BasicIntegrationTest;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import static org.hamcrest.core.IsNot.not;
import org.junit.After;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;


/**
 * Ad hoc test for checking {@link MailServiceImpl}. This test should generally be marked as "ignored". Suitable only
 * for manual checking.
 * <p/>
 * If you need to reuse "log checking" logic, please, consider creation of general junit rule
 * as described in http://tux2323.blogspot.sk/2011/06/test-logging-via-junit-rule.html.
 */
@Ignore
public class MailServiceImplTest extends BasicIntegrationTest {

    @Autowired
    private MailService mailService;
    private TestAppender appender;
    private Logger logger;

    @Before
    public void setupLoggerAppender() {
        final TestAppender appender = new TestAppender();
        final Logger logger = Logger.getRootLogger();
        logger.addAppender(appender);
        this.logger = logger;
        this.appender = appender;
    }


    @After
    public void removeLoggerAppender() {
        logger.removeAppender(appender);
    }

    @Test
    public void testSend() throws Exception {

        // enter you email address at this place
        mailService.send(createTestMailMessage("jumarko@gmail.com"));

        final List<LoggingEvent> log = appender.getLog();
        // no error should occur
        for (LoggingEvent loggingEvent : log) {
            assertThat(loggingEvent.getLevel(), not(Level.ERROR));
        }

    }

    private SimpleMailMessage createTestMailMessage(String userMail) {
        final SimpleMailMessage activationMessage = new SimpleMailMessage();

        activationMessage.setFrom("poptavka1@gmail.com");
        activationMessage.setTo(userMail);

        activationMessage.setSubject("Poptavka MailService Test");

        activationMessage.setText("Test of mail service via amazon SES");
        return activationMessage;

    }


    private static class TestAppender extends AppenderSkeleton {
        private final List<LoggingEvent> log = new ArrayList<LoggingEvent>();

        @Override
        public boolean requiresLayout() {
            return false;
        }

        @Override
        protected void append(final LoggingEvent loggingEvent) {
            log.add(loggingEvent);
        }

        @Override
        public void close() {
        }

        public List<LoggingEvent> getLog() {
            return new ArrayList<LoggingEvent>(log);
        }
    }


}
