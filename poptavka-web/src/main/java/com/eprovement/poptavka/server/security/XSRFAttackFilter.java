/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.server.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.web.filter.GenericFilterBean;

/**
 * TODO LATER: we have to fix this filter since it has never done the right job.
 * See also: http://blog.technowobble.com/2010/05/gwt-and-spring-security.html.
 */
public class XSRFAttackFilter extends GenericFilterBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(XSRFAttackFilter.class.getName());

    @Override
    public void doFilter(ServletRequest req,
            ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String cookieId = request.getParameter("JSESSIONID");
        String sessionId = request.getSession().getId();

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("cookieId={} / sessionId={}", cookieId, sessionId);
        }

        // if the user is authenticated, the cookie session id and the id sent as a request param must match
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // TODO LATER: following code does not work - cookieId is always null - why ?
        if (authentication != null && !sessionId.equals(cookieId)) {
            SecurityContextHolder.clearContext();
            throw new SessionAuthenticationException("Invalid session - you have been logged out!");
        }

        chain.doFilter(request, response);
    }
}
