/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.service.message;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.domain.settings.NotificationItem;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.service.message.settings.NotificationItemService;
import cz.poptavka.sample.service.user.ClientService;
import java.util.List;
import static org.hamcrest.core.Is.is;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DataSet(path = {
        "classpath:cz/poptavka/sample/domain/user/UsersDataSet.xml",
        "classpath:cz/poptavka/sample/domain/register/RegisterDataSet.xml",
        "classpath:cz/poptavka/sample/domain/settings/NotificationItemsDataSet.xml" },
        dtd = "classpath:test.dtd")
public class NotificationItemServiceIntegrationTest extends DBUnitBaseTest {

    @Autowired
    private NotificationItemService notificationItemService;
    @Autowired
    private ClientService clientService;


    @Test
    public void testGetUsersNotification() {
        final Client client1 = this.clientService.getById(111111111L);
        final List<NotificationItem> clientNotificationItems =
                client1.getBusinessUser().getSettings().getNotificationItems();
        Assert.assertThat(clientNotificationItems.size(), is(5));
    }
}
