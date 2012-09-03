package com.eprovement.poptavka.client.common.security;

import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.ExceptionUtils;
import com.eprovement.poptavka.shared.exceptions.SecurityDialogBoxes;
import com.eprovement.poptavka.shared.exceptions.SecurityDialogBoxes.AccessDeniedBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.mvp4g.client.event.EventBusWithLookup;
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
        // TODO ivlcek - all errors should be sent by email to support team. Add this method to every alertBox
        if (caught == null) {
            ASYNC_CALLBACK_LOGGER.log(Level.SEVERE, "Received parameter 'caught' is null ?!!");
            return;
        }
        int errorResponse = 0;
        if (caught instanceof StatusCodeException) {
            errorResponse = ((StatusCodeException) caught).getStatusCode();
        } else {
            // If access is denied, display an error message box (AccessDeniedBox) - HTTP 403
            if (caught instanceof ApplicationSecurityException) {
                ASYNC_CALLBACK_LOGGER.log(Level.WARNING, "AppSecException access denied exception" + caught.toString());
                onAccessDenied();
                return;
            }
            onServiceFailure(caught);
            return;
        }
        // If not authorized display LoginPopupWindow - HTTP 401
        if (errorResponse == Response.SC_UNAUTHORIZED) {
            ASYNC_CALLBACK_LOGGER.log(Level.WARNING, "isNotAuthrized: message status=" + caught.toString());
            onAuthorizationExpected(null); // instead of null, send message to user that he is not authorized.
            return;
        }
        // If access is denied, display an error message box (AccessDeniedBox) - HTTP 403
        if (errorResponse == Response.SC_FORBIDDEN) {
            ASYNC_CALLBACK_LOGGER.log(Level.WARNING, "isAccessDenied: caught=" + caught.toString());
            onAccessDenied();
            return;
        }
        // If page not found - HTTP 404
        if (errorResponse == Response.SC_NOT_FOUND) {
            ASYNC_CALLBACK_LOGGER.log(Level.WARNING, "isPageNotFound: caught=" + caught.toString());
            new SecurityDialogBoxes.AlertBox(MSGS.pageNotFound()).show();
            return;
        }
        // If bad request - HTTP 400
        if (errorResponse == Response.SC_BAD_REQUEST) {
            ASYNC_CALLBACK_LOGGER.log(Level.WARNING, "isBadRequest: caught=" + caught.toString());
            new SecurityDialogBoxes.AlertBox(MSGS.badRequest()).show();
            return;
        }
        // If internal error - HTTP 500
        if (errorResponse == Response.SC_INTERNAL_SERVER_ERROR) {
            ASYNC_CALLBACK_LOGGER.log(Level.SEVERE, "isInternalError: caught=" + caught.toString());
            new SecurityDialogBoxes.AlertBox(MSGS.internalError()).show();
            return;
        }
        // If service is temporarily unavailable - HTTP 503
        if (errorResponse == Response.SC_SERVICE_UNAVAILABLE) {
            ASYNC_CALLBACK_LOGGER.log(Level.SEVERE, "isServiceUnavailable: caught=" + caught.toString());
            new SecurityDialogBoxes.AlertBox(MSGS.serviceUnavailable()).show();
            return;
        }
        // Other HTTP errors
        onServiceFailure(caught);
    }

    /**
     * User can override this method to handle failures. Default implementation do nothing to not bother client with
     * implementation of stuff that is not needed in most of situations.
     *
     * @param caught
     */
    protected void onServiceFailure(Throwable caught) {
        // TODO: more reasonable message must be provided -> probably reference to error id with possibility to send
        // message to our customer support team.
        // error id should be generated in RpcExceptionAspect
        ASYNC_CALLBACK_LOGGER.log(Level.SEVERE, "Error status response: caught=" + caught.toString());
//        new SecurityDialogBoxes.AlertBox(MSGS.serverError()).show(); // display to customers when in production
        // TODO: remove this when in production and use the code above
        new SecurityDialogBoxes.AlertBox(ExceptionUtils.getFullErrorMessage(caught)).show();
    }

    /*
     * (non-Javadoc) @see com.gwtsecurity.client.SecurityCallbackHandler#onAuthorizationExpected(java.lang.String)
     */
    @Override
    public void onAuthorizationExpected(String messageForUser) {
        // TODO ivlcek - we could display a message to user that he is not authorized for this action
        lookupEventbus.dispatch("login");
    }

    /*
     * (non-Javadoc) @see com.gwtsecurity.client.SecurityCallbackHandler#onAccessDenied()
     */
    @Override
    public void onAccessDenied() {
        System.err.println("Accessdeniedbox");
        new AccessDeniedBox().show();
    }
}
