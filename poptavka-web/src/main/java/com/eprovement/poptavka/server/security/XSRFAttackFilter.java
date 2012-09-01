/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.server.security;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.web.filter.GenericFilterBean;

public class XSRFAttackFilter extends GenericFilterBean {

    private static final Logger LOGGER = Logger.getLogger(XSRFAttackFilter.class.getName());

    @Override
    public void doFilter(ServletRequest req,
            ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String cookieId = request.getParameter("JSESSIONID");
        String sessionId = request.getSession().getId();

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("cookieId=" + cookieId + " / sessionId=" + sessionId);
        }

        // if the user is authenticated, the cookie session id and the id sent as a request param must match
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && !sessionId.equals(cookieId)) {
            SecurityContextHolder.clearContext();
            throw new SessionAuthenticationException("Invalid session - you have been logged out!");
        }

        chain.doFilter(request, response);
    }
}
