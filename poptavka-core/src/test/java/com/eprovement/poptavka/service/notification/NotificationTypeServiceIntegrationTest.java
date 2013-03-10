/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.notification;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.settings.Notification;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DataSet(path = "classpath:com/eprovement/poptavka/domain/register/RegisterDataSet.xml",
        dtd = "classpath:test.dtd")
public class NotificationTypeServiceIntegrationTest extends DBUnitIntegrationTest {

    @Autowired
    private NotificationTypeService notificationTypeService;

    @Test
    public void testGetNotificationsForUser() throws Exception {
        checkNotifications(notificationTypeService.getNotificationsForUser(),
                "new.message.operator", "new.info", "new.message");
    }

    @Test
    public void testGetNotificationsForClient() throws Exception {
        checkNotifications(notificationTypeService.getNotificationsForClient(),
                "new.message.operator", "new.info", "new.message", "new.offer", "demand.status.changed");
    }

    @Test
    public void testGetNotificationsForSupplier() throws Exception {
        checkNotifications(notificationTypeService.getNotificationsForSupplier(),
                "new.message.operator", "new.info", "new.message", "new.demand", "offer.status.changed");
    }


    //--------------------------------------------------- HELPER METHODS -----------------------------------------------
    private void checkNotifications(List<Notification> notifications, String... expectedNotificationsCodes) {
        assertThat("Unexpected count of notifications", notifications.size(), is(expectedNotificationsCodes.length));
        for (final String code : expectedNotificationsCodes) {
            assertTrue("notification with code=" + code + " has not been found",
                    CollectionUtils.exists(notifications, new Predicate() {
                        @Override
                        public boolean evaluate(Object object) {
                            return code.equals(((Notification) object).getCode());
                        }
                    }
                ));
        }
    }
}
