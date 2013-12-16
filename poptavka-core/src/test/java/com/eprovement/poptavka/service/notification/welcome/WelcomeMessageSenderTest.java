package com.eprovement.poptavka.service.notification.welcome;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.eprovement.poptavka.domain.enums.NotificationType;
import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.domain.settings.Notification;
import com.eprovement.poptavka.domain.settings.NotificationItem;
import com.eprovement.poptavka.domain.settings.Settings;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.service.notification.NotificationSender;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class WelcomeMessageSenderTest {

    private final User welcomeUser = new User();


    @Before
    public void setUp() throws Exception {
        final Notification welcomeNotification = new Notification();
        welcomeNotification.setNotificationType(NotificationType.WELCOME);
        final Settings userSettings = new Settings();
        final NotificationItem welcomeNotificationItem = new NotificationItem();
        welcomeNotificationItem.setNotification(welcomeNotification);
        welcomeNotificationItem.setPeriod(Period.INSTANTLY);
        userSettings.setNotificationItems(asList(welcomeNotificationItem));
        welcomeUser.setSettings(userSettings);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWelcomeMessageWithoutSenderShouldNotBeAllowed() throws Exception {
        new WelcomeMessageSender(Collections.<NotificationSender>emptyList());
    }

    @Test
    public void testSendWelcomeMessageWithOneSender() throws Exception {
        final NotificationSender singleNotificationSender = mock(NotificationSender.class);
        final WelcomeMessageSender welcomeSender = new WelcomeMessageSender(asList(singleNotificationSender));
        welcomeSender.sendWelcomeMessage(welcomeUser);
        verifySendNotification(singleNotificationSender);
    }

    @Test
    public void testSendWelcomeMessageShouldSendTwoMessagesToUserWithTwoWelcomeNotifications() throws Exception {
        final Notification anotherWelcomeNotification = new Notification();
        anotherWelcomeNotification.setNotificationType(NotificationType.WELCOME);
        final NotificationItem userNotification = new NotificationItem();
        userNotification.setNotification(anotherWelcomeNotification);
        welcomeUser.getSettings().setNotificationItems(new ArrayList<NotificationItem>(
                CollectionUtils.union(welcomeUser.getSettings().getNotificationItems(), asList(userNotification))));
        final NotificationSender singleNotificationSender = mock(NotificationSender.class);
        final WelcomeMessageSender welcomeSender = new WelcomeMessageSender(asList(singleNotificationSender));
        welcomeSender.sendWelcomeMessage(welcomeUser);
        verifySendNotification(singleNotificationSender, 2);
    }

    @Test
    public void testSendWelcomeMessageWithoutWelcomeNotificationSetting() throws Exception {
        final NotificationSender singleNotificationSender = mock(NotificationSender.class);
        final WelcomeMessageSender welcomeSender = new WelcomeMessageSender(asList(singleNotificationSender));
        // remove the welcome notification from user settings
        welcomeUser.getSettings().setNotificationItems(Collections.<NotificationItem>emptyList());
        welcomeSender.sendWelcomeMessage(welcomeUser);
        verifyZeroInteractions(singleNotificationSender);
    }

    @Test
    public void testSendWelcomeMessageWithMultipleSenders() throws Exception {
        final NotificationSender emailNotificationSender = mock(NotificationSender.class);
        final NotificationSender messageNotificationSender = mock(NotificationSender.class);
        final NotificationSender smsNotificationSender = mock(NotificationSender.class);
        final WelcomeMessageSender welcomeSender = new WelcomeMessageSender(
                asList(emailNotificationSender, messageNotificationSender, smsNotificationSender));
        welcomeSender.sendWelcomeMessage(welcomeUser);
        verifySendNotification(emailNotificationSender);
        verifySendNotification(messageNotificationSender);
        verifySendNotification(smsNotificationSender);
    }


    //--------------------------------------------------- HELPER METHODS -----------------------------------------------
    private void verifySendNotification(NotificationSender notificationSender) {
        verifySendNotification(notificationSender, 1);
    }

    private void verifySendNotification(NotificationSender notificationSender, int times) {
        verify(notificationSender, times(times)).sendNotification(eq(welcomeUser), any(Notification.class),
                (Map<String, String>) isNull());
    }
}
