package com.eprovement.poptavka.service.jobs.notification;

import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.service.jobs.base.Job;
import org.apache.commons.lang.Validate;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Job responsible for sending weekly notifications.
 * TODO LATER juraj: introduce more generic jobs parameterized by period.
 */
public class WeeklyNotificationSender implements Job {
    private final NotificationSenderHelper notificationSenderHelper;

    public WeeklyNotificationSender(NotificationSenderHelper notificationSenderHelper) {
        Validate.notNull(notificationSenderHelper, "notificationSenderHelper cannot be null!");
        this.notificationSenderHelper = notificationSenderHelper;
    }

    /**
     * Sends notifications about new messages once per week.
     */
    @Override
    @Scheduled(cron = EVERY_WEEK)
    public void execute() {
        notificationSenderHelper.sendNotifications(Period.WEEKLY);
    }

    @Override
    public String description() {
        return "WeeklyNotificationSender is responsible for sending notifications to end users in a weekly fashion. ";
    }
}
