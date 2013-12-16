/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.rest.support;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.apache.commons.lang.Validate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class MockMvcSupport {

    private final MockMvc mockMvc;

    public MockMvcSupport(MockMvc mockMvc) {
        Validate.notNull(mockMvc, "mockMvc cannot be null!");
        this.mockMvc = mockMvc;
    }

    /**
     * The preferred way how to create instance of {@link MockMvcSupport}.
     * Should be called in before test method.
     * @param wac web application context.
     * @return new build mock mvc support instance
     */
    public static MockMvcSupport build(WebApplicationContext wac) {
        return new MockMvcSupport(MockMvcBuilders.webAppContextSetup(wac).build());
    }

    public MockMvc innerMock() {
        return mockMvc;
    }

    public ResultActions performRequest(MockHttpServletRequestBuilder request) {
        try {
            return innerMock().perform(request.contentType(MediaType.APPLICATION_JSON)).andDo(print());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
