package com.eprovement.poptavka.service.jobs.notification;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.eprovement.poptavka.base.integration.BasicIntegrationTest;
import com.eprovement.poptavka.domain.enums.CommonAccessRoles;
import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.message.MessageService;
import com.eprovement.poptavka.service.notification.NotificationService;
import com.eprovement.poptavka.service.usermessage.UserMessageService;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class NotificationSenderHelperTest extends BasicIntegrationTest {

    private static final String USER_EMAIL = "mytest@myemail.com";
    private NotificationSenderHelper notificationSenderHelper;
    @Autowired private MessageService messageService;
    @Autowired private GeneralService generalService;
    @Autowired private UserMessageService userMessageService;

    /* Implement stub for notification because Mockito does not support verification of varargs via ArgumentCaptor. */
    private static class NotificationServiceMock implements NotificationService {
        private UserMessage[] lastUserMessages;
        @Override
        public void notifyUserNewMessage(Period expectedPeriod, UserMessage... newMessages) {
            this.lastUserMessages = newMessages;
        }
    }
    private NotificationServiceMock notificationServiceMock = new NotificationServiceMock();

    private Message testMessage1;
    private Message testMessage2;
    private Message testMessage3;

    @Before
    public void setUp() throws Exception {
        this.notificationSenderHelper = new NotificationSenderHelper(userMessageService, notificationServiceMock);
        final User user = saveOrGetUser();
        testMessage1 = messageService.newThreadRoot(user);
        testMessage2 = messageService.newThreadRoot(user);
        testMessage3 = messageService.newThreadRoot(user);
    }


    @Test
    public void testDailyNotificationSent() throws Exception {
        // testMessage1 should have actual date
        updateMessageCreatedDate(testMessage2, 23);
        updateMessageCreatedDate(testMessage3, 25);
        notificationSenderHelper.sendNotifications(Period.DAILY);

        // two message have been "created" in past 24 hours so two notifications should have been sent
        checkNotificationMail(2);
    }

    @Test
    public void testWeeklyNotificationSent() throws Exception {
        updateMessageCreatedDate(testMessage1, 77);
        updateMessageCreatedDate(testMessage2, 240);
        updateMessageCreatedDate(testMessage3, 169);
        notificationSenderHelper.sendNotifications(Period.WEEKLY);

        // one message has been "created" in past 7 days so one notification should have been sent
        checkNotificationMail(1);
    }

    //--------------------------------------------------- HELPER METHODS -----------------------------------------------
    private void updateMessageCreatedDate(Message message, int hoursBackInPast) {
        message.setCreated(new DateTime().minusHours(hoursBackInPast).toDate());
        messageService.update(message);
    }

    private void checkNotificationMail(int expectedNumberOfNotifications) {
        assertNotNull("UserMessages passed to the NotificationService should not be null!",
                notificationServiceMock.lastUserMessages);
        for (int i = 0; i < expectedNumberOfNotifications; i++) {
            assertThat(notificationServiceMock.lastUserMessages[i].getUser().getEmail(), is(USER_EMAIL));
        }
    }

    private User saveOrGetUser() {
        final Search userSearch = new Search(User.class);
        userSearch.addFilterEqual("email", USER_EMAIL);
        final List<User> search = generalService.search(userSearch);
        if (CollectionUtils.isNotEmpty(search)) {
            return search.get(0);
        }

        final User user = new User(USER_EMAIL, "password");
        final AccessRole accessRole = new AccessRole();
        accessRole.setCode(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE);
        generalService.save(accessRole);
        user.setAccessRoles(Arrays.asList(accessRole));
        generalService.save(user);
        return user;
    }
}
