/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.register;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.product.Service;
import com.eprovement.poptavka.domain.register.Register;
import com.eprovement.poptavka.domain.register.Registers;
import com.eprovement.poptavka.domain.settings.Notification;
import java.util.List;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DataSet(path = "classpath:com/eprovement/poptavka/domain/register/RegisterDataSet.xml", dtd = "classpath:test.dtd")
public class RegisterServiceIntegrationTest extends DBUnitIntegrationTest {

    @Autowired
    private RegisterService registerService;

    @Test
    public void testGetAllValuesForRegister() {
        final List<Notification> allNotifications = this.registerService.getAllValues(Notification.class);
        assertNotNull(allNotifications);
        assertThat("Unexpected count of notifications", allNotifications.size(), Is.is(11));
    }

    @Test
    public void testGetRegisterValueByCode() {
        final Notification newMessageClient = this.registerService.getValue(
                Registers.Notification.NEW_MESSAGE.getCode(), Notification.class);
        checkRegister(newMessageClient, 1L, "new.message");

        final Service serviceClassic = this.registerService.getValue(Registers.Service.CLASSIC, Service.class);
        checkRegister(serviceClassic, 4L, "CLASSIC");
    }

    private <T extends Register> void checkRegister(T register, long expectedId, String expectedCode) {
        assertNotNull(register);
        assertThat("Unexpected register found", register.getId(), Is.is(expectedId));
        assertThat("Unexpected register's code found", register.getCode(), Is.is(expectedCode));
    }
}
