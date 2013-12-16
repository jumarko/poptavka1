/*
 * Copyright (C) 2012, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles Authentication failure.
 * @author Juraj Martinka Date: 21.8.11
 */
public class PoptavkaAuthenticationFailureHandler implements
        AuthenticationFailureHandler {

    /**
     * Sends an error on authentication failure.
     * @param request
     * @param response
     * @param exception
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication not accepted");
    }
}
