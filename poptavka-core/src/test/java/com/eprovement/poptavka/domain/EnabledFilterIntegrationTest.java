/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.domain.settings.Notification;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.service.register.RegisterService;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Test if Hibernate filter on enabled flag is working properly for all Domain objects.
 * @see com.eprovement.poptavka.domain.common.DomainObject
 */
@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/register/RegisterDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml" },
        dtd = "classpath:test.dtd")
public class EnabledFilterIntegrationTest extends DBUnitIntegrationTest {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private LocalityService localityService;


    @Test
    public void testGetAllEnabledNotifications() {
        final List<Notification> enabledNotifications = this.registerService.getAllValues(Notification.class);
        assertNotNull(enabledNotifications);
        assertThat("Unexpected count of notifications", enabledNotifications.size(), Is.is(11));
        for (Notification notification : enabledNotifications) {
            assertTrue("notification is expected to be enabled but it is not: " + notification,
                    notification.isEnabled());
        }
    }

    @Test
    public void testGetEnabledLocalities() throws Exception {
        final List<Locality> enabledLocalities = localityService.getLocalities(LocalityType.COUNTRY);
        assertNotNull("enabled localities should not be null", enabledLocalities);
        assertThat("Unexpected count of localities", enabledLocalities.size(), Is.is(1));
        for (Locality locality : enabledLocalities) {
            assertTrue("locality is expected to be enabled but it is not: " + locality,
                    locality.isEnabled());
        }

    }
}
