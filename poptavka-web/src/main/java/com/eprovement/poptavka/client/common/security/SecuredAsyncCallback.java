/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common.security;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.mvp4g.client.event.EventBusWithLookup;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A new AsyncCallback that can handle security responses.
 *
 * @author ivlcek
 */
public abstract class SecuredAsyncCallback<T> implements AsyncCallback<T>, SecurityCallbackHandler {

    protected static final Logger ASYNC_CALLBACK_LOGGER = Logger.getLogger("SecuredAsyncCallback");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private EventBusWithLookup lookupEventbus;

    public SecuredAsyncCallback(EventBusWithLookup lookupEventbus) {
        this.lookupEventbus = lookupEventbus;
    }

    /**
     * This method handles all erroneous HTTP responses 4xx and 5xx and displayes reasonable message to User. If user is
     * not authorized the LoginPopupView is displayed
     *
     * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
     */
    @Override
    public final void onFailure(final Throwable caught) {
        // TODO LATER ivlcek - random generator of error Id should be moved to RpcExceptionAspect.java
        String errorId = Long.toString(new Date().getTime());
        if (caught == null) {
            ASYNC_CALLBACK_LOGGER.log(Level.SEVERE, "ErrorID=" + errorId + ", Received parameter 'caught' is null ?!!");
            return;
        } else {

            // If access is denied, display an error message box (AccessDeniedBox) - HTTP 403
            if (caught instanceof ApplicationSecurityException) {
                ASYNC_CALLBACK_LOGGER.log(Level.WARNING, "ErrorID=" + errorId
                        + ", AppSecException access denied exception" + caught.toString());
                displayError(Response.SC_FORBIDDEN, errorId);
                return;
            }

            int errorResponse = 0;
            if (caught instanceof StatusCodeException) {
                errorResponse = ((StatusCodeException) caught).getStatusCode();

                // If not authorized display LoginPopupWindow - HTTP 401
                if (errorResponse == Response.SC_UNAUTHORIZED) {
                    ASYNC_CALLBACK_LOGGER.log(Level.WARNING, "ErrorID=" + errorId + ", isNotAuthrized: message status="
                            + caught.toString());
                    onAuthorizationExpected(null); // instead of null, send message to user that he is not authorized.
                    return;
                }
                // If access is denied, display an error message box (AccessDeniedBox) - HTTP 403
                if (errorResponse == Response.SC_FORBIDDEN) {
                    ASYNC_CALLBACK_LOGGER.log(Level.WARNING, "ErrorID=" + errorId + ", isAccessDenied: caught="
                            + caught.toString());
                    onAccessDenied(errorId);
                    return;
                }
                // If page not found - HTTP 404
                if (errorResponse == Response.SC_NOT_FOUND) {
                    ASYNC_CALLBACK_LOGGER.log(Level.WARNING, "ErrorID=" + errorId + ", isPageNotFound: caught="
                            + caught.toString());
                    displayError(Response.SC_NOT_FOUND, errorId);
                    return;
                }
                // If bad request - HTTP 400
                if (errorResponse == Response.SC_BAD_REQUEST) {
                    ASYNC_CALLBACK_LOGGER.log(Level.WARNING, "ErrorID=" + errorId + ", isBadRequest: caught="
                            + caught.toString());
                    displayError(Response.SC_BAD_REQUEST, errorId);
                    return;
                }
                // If internal error - HTTP 500
                if (errorResponse == Response.SC_INTERNAL_SERVER_ERROR) {
                    ASYNC_CALLBACK_LOGGER.log(Level.SEVERE, "ErrorID=" + errorId + ", isInternalError: caught="
                            + caught.toString());
                    displayError(Response.SC_INTERNAL_SERVER_ERROR, errorId);
                    return;
                }
                // If service is temporarily unavailable - HTTP 503
                if (errorResponse == Response.SC_SERVICE_UNAVAILABLE) {
                    ASYNC_CALLBACK_LOGGER.log(Level.SEVERE, "ErrorID=" + errorId + ", isServiceUnavailable: caught="
                            + caught.toString());
                    displayError(Response.SC_SERVICE_UNAVAILABLE, errorId);
                    return;
                }

            }
            // Other HTTP errors
            onServiceFailure(caught, errorResponse, errorId);
        }   // end of else if
    }

    /**
     * User can override this method to handle failures. Default implementation do nothing to not bother client with
     * implementation of stuff that is not needed in most of situations.
     *
     * @param caught
     */
    protected void onServiceFailure(Throwable caught, int errorResponse, String errorId) {
        // TODO LATER: more reasonable message must be provided like error id with possibility to send
        // message to our customer support team.
        // error id should be generated in RpcExceptionAspect
        ASYNC_CALLBACK_LOGGER.log(Level.SEVERE, "ErrorID=" + errorId + ", Error status response: caught="
                + caught.toString());
        displayError(errorResponse, errorId);
    }

    /*
     * (non-Javadoc) @see com.gwtsecurity.client.SecurityCallbackHandler#onAuthorizationExpected(java.lang.String)
     */
    @Override
    public void onAuthorizationExpected(String messageForUser) {
        lookupEventbus.dispatch("login", Constants.NONE);
    }

    /*
     * (non-Javadoc) @see com.gwtsecurity.client.SecurityCallbackHandler#onAccessDenied()
     */
    @Override
    public void onAccessDenied(String errorId) {
        displayError(Response.SC_FORBIDDEN, errorId);
    }

    /**
     * Display error to user.
     *
     * @param errorResponse error response code
     */
    private void displayError(int errorResponseCode, String errorId) {
        lookupEventbus.dispatch("displayError", errorResponseCode, errorId);
    }
}
