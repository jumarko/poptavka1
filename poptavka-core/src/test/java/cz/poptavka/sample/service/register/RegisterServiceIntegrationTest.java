/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.service.register;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.domain.settings.Notification;
import cz.poptavka.sample.domain.settings.NotificationType;
import java.util.List;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DataSet(path = "classpath:cz/poptavka/sample/domain/register/RegisterDataSet.xml", dtd = "classpath:test.dtd")
public class RegisterServiceIntegrationTest extends DBUnitBaseTest {

    @Autowired
    private RegisterService registerService;

    @Test
    public void testGetAllValuesForRegister() {
        final List<Notification> allNotifications = this.registerService.getAllValues(Notification.class);
        Assert.assertNotNull(allNotifications);
        Assert.assertThat("Unexpected count of notifications", allNotifications.size(), Is.is(11));

    }

    @Test
    public void testGetRegisterValueByCode() {
        final Notification newMessageClient = this.registerService.getValue("new.message.client", Notification.class);
        Assert.assertNotNull(newMessageClient);
        Assert.assertThat("Unexcpected notification found", newMessageClient.getId(), Is.is(10L));
        Assert.assertThat("Incorrect notification type",
                newMessageClient.getNotificationType(), Is.is(NotificationType.CLIENT));
        Assert.assertThat("Incorrect name",
                newMessageClient.getName(), Is.is("Nová správa"));
    }
}
