package com.eprovement.poptavka.service.notification;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.eprovement.poptavka.domain.settings.Notification;
import org.junit.Test;

import java.util.HashMap;

public class NotificationSenderUtilsTest {


    @Test
    public void expandMessageBody() throws Exception {
        final Notification notification = new Notification();
        notification.setMessageTemplate("Hello ${user}, how are you?");
        final String expandedMessage = NotificationSenderUtils.expandMessageBody(notification,
                new HashMap<String, String>() { { put("user", "Big Boss"); } });
        assertThat("message template has not been expadned properly",
                expandedMessage, is("Hello Big Boss, how are you?"));
    }

    @Test
    public void expandMessageBodyMultiplePlaceholders() throws Exception {
        final Notification notification = new Notification();
        notification.setMessageTemplate("Hello ${user}, are you ${adjective}\nor ${adjective2}?");
        final String expandedMessage = NotificationSenderUtils.expandMessageBody(notification,
                    new HashMap<String, String>() { {
                    put("user", "Big Boss");
                    put("adjective", "crazy");
                    put("adjective2", "lazy");
                }
            });
        assertThat("message template has not been expadned properly",
                expandedMessage, is("Hello Big Boss, are you crazy\nor lazy?"));
    }

    @Test
    public void expandMessageBodyWithoutPlaceholders() throws Exception {
        final Notification notification = new Notification();
        notification.setMessageTemplate("Hello user, are you sure?");
        final String expandedMessage = NotificationSenderUtils.expandMessageBody(notification,
                new HashMap<String, String>() { { put("user", "Big Boss"); } });
        assertThat("message template has not been expadned properly",
                expandedMessage, is("Hello user, are you sure?"));
    }
}
