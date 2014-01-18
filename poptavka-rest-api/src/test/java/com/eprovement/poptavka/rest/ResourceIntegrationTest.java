/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.rest;


import com.eprovement.poptavka.base.integration.DBUnitBaseTest;
import com.eprovement.poptavka.rest.support.MockMvcSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

/**
 * Basic class for resource integration tests to avoid code repetition.
 * You should always prefer extension of this class instead
 * of {@link com.eprovement.poptavka.base.integration.DBUnitBaseTest} for resources integration tests.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:applicationContext-rest-test.xml")
@ActiveProfiles("test")
public abstract class ResourceIntegrationTest extends DBUnitBaseTest {

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    protected ObjectMapper jsonObjectMapper;

    protected MockMvcSupport mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcSupport.build(wac);
    }
}
