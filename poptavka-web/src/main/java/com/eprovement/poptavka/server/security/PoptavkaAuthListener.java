/*
 * Copyright (C) 2012, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;

/**
 * This class represent listener to authentication event
 *
 * @author kolkar
 *
 */
public class PoptavkaAuthListener implements ApplicationListener<AbstractAuthenticationEvent> {

    private static final Log LOGGER = LogFactory.getLog(PoptavkaAuthListener.class);

    /**
     * Authenticate user.
     * @param event the AbstractAuthenticationEvent
     */
    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent event) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Authentication event ");
        builder.append(event.getClass().getSimpleName());
        builder.append(": ");
        builder.append(event.getAuthentication().getName());
        builder.append("; details: ");
        builder.append(event.getAuthentication().getDetails());

        if (event instanceof AbstractAuthenticationFailureEvent) {
            builder.append("; exception: ");
            builder.append(((AbstractAuthenticationFailureEvent) event).getException().getMessage());
        }

        LOGGER.warn(builder.toString());

        // Advanced tracking of login, logout and session
        //Session ends
        ApplicationEvent appEvent = (ApplicationEvent) event;
        if (appEvent instanceof HttpSessionDestroyedEvent) {
            String username = "";
            String remoteAddress = null;
            HttpSessionDestroyedEvent logoutEvent = (HttpSessionDestroyedEvent) appEvent;
            SecurityContext securityContext = (SecurityContext) logoutEvent.
                getSession().getAttribute("SPRING_SECURITY_CONTEXT");
            if (securityContext != null) {
                username = (String) logoutEvent.getSession().getAttribute("SPRING_SECURITY_LAST_USERNAME");
                WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) securityContext.
                    getAuthentication().getDetails();
                LOGGER.warn("applicationEvent is HttpSessionDestroyedEvent, username="
                    + username + ", remoteAddress=" + remoteAddress + ", securityContext!=null");
                if (webAuthenticationDetails != null) {
                    remoteAddress = webAuthenticationDetails.getRemoteAddress();
                }
                //Log action
                LOGGER.warn("applicationEvent is HttpSessionDestroyedEvent, username="
                    + username + ", remoteAddress=" + remoteAddress);
            }
            return;
        }
        //Success
        if (appEvent instanceof InteractiveAuthenticationSuccessEvent) {
            InteractiveAuthenticationSuccessEvent successEvent = (InteractiveAuthenticationSuccessEvent) appEvent;
            WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) successEvent.
                getAuthentication().getDetails();
            String username = successEvent.getAuthentication().getName();
            //Log action
            LOGGER.warn("applicationEvent is InteractiveAuthenticationSuccessEvent: username=" + username);
            return;
        }
        //Bad credentials
        if (appEvent instanceof AuthenticationFailureBadCredentialsEvent) {
            AuthenticationFailureBadCredentialsEvent failureEvent = (AuthenticationFailureBadCredentialsEvent) appEvent;
            WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) failureEvent.
                getAuthentication().getDetails();
            String username = failureEvent.getAuthentication().getName();
            //Log action
            LOGGER.warn("applicationEvent is AuthenticationFailureBadCredentialsEvent: username=" + username);
            return;
        }

    }
}
