/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.notification;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.settings.NotificationItem;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.service.user.ClientService;
import java.util.List;
import static org.hamcrest.core.Is.is;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/register/RegisterDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml" },
        dtd = "classpath:test.dtd")
public class NotificationSettingsIntegrationTest extends DBUnitIntegrationTest {

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
