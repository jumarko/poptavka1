package com.eprovement.poptavka.service.jobs.notification;

import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.service.jobs.base.Job;
import org.apache.commons.lang.Validate;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Job responsible for sending daily notifications.
 * TODO LATER juraj: introduce more generic jobs parameterized by period.
 */
public class DailyNotificationSender implements Job {
    private final NotificationSenderHelper notificationSenderHelper;

    public DailyNotificationSender(NotificationSenderHelper notificationSenderHelper) {
        Validate.notNull(notificationSenderHelper, "notificationSenderHelper cannot be null!");
        this.notificationSenderHelper = notificationSenderHelper;
    }

    /**
     * Sends notifications about new messages once per day.
     */
    @Override
    @Scheduled(cron = EVERY_DAY)
    public void execute() {
        notificationSenderHelper.sendNotifications(Period.DAILY);
    }

    @Override
    public String description() {
        return "DailyNotificationSender is responsible for sending notifications to end users in a daily fashion. ";
    }
}
