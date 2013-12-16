/*
 * Copyright (C) 2012, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * Handles successfull logout.
 * @author ivlcek
 */
public class PoptavkaLogoutSuccessHandler implements LogoutSuccessHandler {

    /**
     * Sends an successfull status on successfull logout.
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.sendError(HttpServletResponse.SC_OK, "Logout completed");
    }
}
