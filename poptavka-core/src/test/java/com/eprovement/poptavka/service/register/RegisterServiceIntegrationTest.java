/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.register;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.product.Service;
import com.eprovement.poptavka.domain.register.Register;
import com.eprovement.poptavka.domain.register.Registers;
import com.eprovement.poptavka.domain.settings.Notification;
import java.util.List;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DataSet(path = "classpath:com/eprovement/poptavka/domain/register/RegisterDataSet.xml", dtd = "classpath:test.dtd")
public class RegisterServiceIntegrationTest extends DBUnitIntegrationTest {

    @Autowired
    private RegisterService registerService;

    @Test
    public void testGetAllValuesForRegister() {
        final List<Notification> allNotifications = this.registerService.getAllValues(Notification.class);
        Assert.assertNotNull(allNotifications);
        Assert.assertThat("Unexpected count of notifications", allNotifications.size(), Is.is(12));

    }

    @Test
    public void testGetRegisterValueByCode() {
        final Notification newMessageClient = this.registerService.getValue(Registers.Notification.CLIENT_NEW_MESSAGE,
                Notification.class);
        checkRegister(newMessageClient, 10L, "new.message.client");

        final Service serviceClassic = this.registerService.getValue(Registers.Service.CLASSIC, Service.class);
        checkRegister(serviceClassic, 4L, "classic");
    }

    private <T extends Register> void checkRegister(T register, long expectedId, String expectedCode) {
        Assert.assertNotNull(register);
        Assert.assertThat("Unexcpected register found", register.getId(), Is.is(expectedId));
        Assert.assertThat("Unexcpected register's code found", register.getCode(), Is.is(expectedCode));
    }
}
