/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.service.register;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.domain.product.Service;
import cz.poptavka.sample.domain.register.Register;
import cz.poptavka.sample.domain.register.Registers;
import cz.poptavka.sample.domain.settings.Notification;
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
        final Notification newMessageClient = this.registerService.getValue(Registers.Notification.NEW_MESSAGE_CLIENT,
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
