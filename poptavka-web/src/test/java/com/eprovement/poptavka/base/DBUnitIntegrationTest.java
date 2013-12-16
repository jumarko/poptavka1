/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.base;

import com.eprovement.poptavka.base.integration.DBUnitBaseTest;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-web.xml", "classpath:applicationContext-core.xml" })
@ActiveProfiles("test")
public abstract class DBUnitIntegrationTest extends DBUnitBaseTest {
}
