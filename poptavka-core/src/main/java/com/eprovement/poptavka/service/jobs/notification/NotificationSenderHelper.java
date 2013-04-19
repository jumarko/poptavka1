package com.eprovement.poptavka.service.jobs.notification;

import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.service.notification.NotificationService;
import com.eprovement.poptavka.service.usermessage.UserMessageService;
import org.apache.commons.lang.Validate;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Helper class for notifications job to avoid code repetition for various periods.
 */
class NotificationSenderHelper {

    private final UserMessageService userMessageService;
    private final NotificationService notificationService;

    NotificationSenderHelper(UserMessageService userMessageService, NotificationService notificationService) {
        Validate.notNull(userMessageService, "userMessageService cannot be null!");
        Validate.notNull(notificationService, "notificationService cannot be null!");
        this.userMessageService = userMessageService;
        this.notificationService = notificationService;
    }

    /**
     * Sends notifications about new events related to the given period.
     * <p>
     *     E.g., if period is {@link Period#DAILY} then this method looks for events which occurred for the last
     *     24 hours and sent the notifications about these events to the end users.
     * </p>
     * @param period last period for which the notifications will be sent, can be either {@link Period.DAILY}
     *               or {@link Period.WEEKLY}.
     */
    @Transactional
    public void sendNotifications(Period period) {
        final Date createdDateFrom;
        switch(period) {
            case DAILY:
                createdDateFrom = new DateTime().minusDays(1).toDate();
                break;
            case WEEKLY:
                createdDateFrom = new DateTime().minusWeeks(1).toDate();
                break;
            default:
                throw new IllegalArgumentException("Unsupported period: " + period);
        }

        final Map<User, List<UserMessage>> userMessagesMap =
                userMessageService.getUserMessagesFromDateGroupedByUser(createdDateFrom);
        for (List<UserMessage> userMessages : userMessagesMap.values()) {
            notificationService.notifyUserNewMessage(
                    period, userMessages.toArray(new UserMessage[userMessages.size()]));
        }
    }
}
