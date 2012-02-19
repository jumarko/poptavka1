/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.util.user;

import cz.poptavka.sample.domain.settings.Notification;
import cz.poptavka.sample.domain.settings.NotificationItem;
import cz.poptavka.sample.domain.settings.Period;
import cz.poptavka.sample.domain.user.User;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.junit.Assert;

public final class UserTestUtils {

    private UserTestUtils() {
        // utility class - DO NOT INSTANTIATE!
    }

    /**
     * Checks wheter given {@code user} has assigned {@link cz.poptavka.sample.domain.settings.NotificationItem}
     * representing given {@code expectedNotification}
     * which status (see {@link cz.poptavka.sample.domain.settings.NotificationItem#isEnabled()} is
     * {@code expectedNotificationStatus}
     * and period is {@code expectedNotificationPeriod}.
     */
    public static void checkHasNotification(User user,
            final Notification expectedNotification,
            final boolean expectedNotificationStatus,
            final Period expectedNotificationPeriod) {
        Assert.assertNotNull(user);
        final List<NotificationItem> userNotificationItems = user.getSettings().getNotificationItems();
        Assert.assertNotNull(userNotificationItems);
        Assert.assertNotNull(expectedNotification);

        Assert.assertTrue(
                CollectionUtils.exists(userNotificationItems, new Predicate() {
                    @Override
                    public boolean evaluate(Object object) {
                        final NotificationItem userNotificationItem = ((NotificationItem) object);
                        return userNotificationItem.getNotification().getCode().equals(expectedNotification.getCode())
                                && userNotificationItem.isEnabled() == expectedNotificationStatus
                                && userNotificationItem.getPeriod() == expectedNotificationPeriod;
                    }
                }));
    }
}
