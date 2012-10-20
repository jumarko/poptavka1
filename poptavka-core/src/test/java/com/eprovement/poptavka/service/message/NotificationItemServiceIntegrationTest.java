/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.message;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.settings.NotificationItem;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.service.message.settings.NotificationItemService;
import com.eprovement.poptavka.service.user.ClientService;
import java.util.List;
import static org.hamcrest.core.Is.is;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/register/RegisterDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/settings/NotificationItemsDataSet.xml" },
        dtd = "classpath:test.dtd")
public class NotificationItemServiceIntegrationTest extends DBUnitIntegrationTest {

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
