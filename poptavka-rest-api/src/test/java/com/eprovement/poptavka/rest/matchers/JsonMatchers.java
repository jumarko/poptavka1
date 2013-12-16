/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.rest.matchers;

import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

public final class JsonMatchers {

    private JsonMatchers() {
        throw new AssertionError("Utility class - do not instantiate");
    }

    /**
     * Handy matcher for checking that some json path is not at json object at all.
     * <p>
     *     Example:<br />
     *     Let's have a following json:
     *     <pre>{"name":"John","surname":"Dusky"}</pre>
     *     We can check that "password" attribute is not included via following call:
     *     <pre>andExpect(jsonPathMissing("password"))</pre>
     * </p>
     * @return matcher checking that some json path is missing in json object
     */
    public static ResultMatcher jsonPathMissing(final String expression) {
        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) throws Exception {
                final JsonPath jsonPath = JsonPath.compile(expression);
                try {
                    jsonPath.read(result.getResponse().getContentAsString());
                    throw new AssertionError(
                            String.format("Path '%s' expected to be missing but found in response", expression));
                } catch (InvalidPathException ex) {
                    // expected state - invalid path because of missing attribute
                } catch (IndexOutOfBoundsException ex) {
                    throw new AssertionError(ex.getMessage());
                }
            }
        };
    }

}
