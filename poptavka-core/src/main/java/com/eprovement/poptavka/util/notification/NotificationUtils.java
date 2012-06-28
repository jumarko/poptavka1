/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.util.notification;

import com.google.common.base.Preconditions;
import com.eprovement.poptavka.domain.settings.Notification;
import com.eprovement.poptavka.domain.settings.NotificationItem;
import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.service.register.RegisterService;

/**
 * Contains handy methods for creating notifications and notification items.
 * Dependencies should be passed through constructor but not injected, because required dependency should be already
 * injected in class that uses this utility class.
 */
public class NotificationUtils {

    private final RegisterService registerService;

    public NotificationUtils(RegisterService registerService) {
        Preconditions.checkNotNull(registerService);
        this.registerService = registerService;
    }

    public NotificationItem createInstantNotificationItem(String notificationCode, boolean enabled) {
        final NotificationItem notificationItem = new NotificationItem();
        notificationItem.setNotification(this.registerService.getValue(notificationCode, Notification.class));
        notificationItem.setEnabled(enabled);
        notificationItem.setPeriod(Period.INSTANTLY);
        return notificationItem;
    }

}
