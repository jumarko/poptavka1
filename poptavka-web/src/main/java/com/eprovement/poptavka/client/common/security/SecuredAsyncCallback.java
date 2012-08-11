/**
 *
 */
package com.eprovement.poptavka.client.common.security;

import com.eprovement.poptavka.client.common.login.LoginDialogBox;
import com.eprovement.poptavka.client.common.login.SecurityDialogBoxes;
import com.eprovement.poptavka.client.common.login.SecurityDialogBoxes.AccessDeniedBox;
// TODO ivlcek - Jurajov kod zakomentovany kvoli prechodu na projekt dmartin
//import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
//import com.eprovement.poptavka.shared.exceptions.SecurityDialogBoxes;
import com.eprovement.poptavka.shared.security.SecurityResponseMessage;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.gwt.user.client.ui.PopupPanel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A new AsyncCallback that can handle security responses.
 *
 * @author dmartin
 */
public abstract class SecuredAsyncCallback<T> implements AsyncCallback<T>, SecurityCallbackHandler {

    protected static final Logger ASYNC_CALLBACK_LOGGER = Logger.getLogger("SecuredAsyncCallback");

    /**
     * @see
     * com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
     */
    @Override
    public final void onFailure(final Throwable caught) {

        // TODO ivlcek - Jurajov kod zakomentovany
//        ASYNC_CALLBACK_LOGGER.log(Level.INFO, "Failure in AsyncCallback", caught);
//        if (caught == null) {
//            ASYNC_CALLBACK_LOGGER.log(Level.FINEST, "Received parameter 'caught' is null ?!!");
//            return;
//        } else if (caught instanceof ApplicationSecurityException) {
//
//            new SecurityDialogBoxes.AccessDeniedBox().show();
//            ExceptionUtils.showErrorDialog("Authorization required",
//                    "You cannot access this funcionality.<br />You are logged as user, who does not support this");
//            return;
//        }

        ASYNC_CALLBACK_LOGGER.log(Level.INFO, "OnFailure: caught=" + caught.getMessage());
        System.err.println("SecuredAsyncCallback . onFailure: caught=" + caught.getMessage());
        if (caught == null) {
            ASYNC_CALLBACK_LOGGER.log(Level.FINEST, "Received parameter 'caught' is null ?!!");
            return;
        }

        final SecurityResponseMessage message = SecurityCallbackUtils.extractJsonMessage(caught.getMessage());

        // If not logged, display the logging popup window and stop
        if (isNotAuthorized(message)) {
            System.err.println("isNotAuthrized: message=" + message.toString());
            ASYNC_CALLBACK_LOGGER.log(Level.INFO, "isNotAuthrized: message=" + message.toString());
            onAuthorizationExpected(message.getLoginFormUrl());
            return;
        }

        // If access is denied, display an error message box (AccessDeniedBox)
        if (isAccessDenied(caught)) {
            System.err.println("isAccessDenied: caught=" + caught.toString());
            ASYNC_CALLBACK_LOGGER.log(Level.INFO, "isAccessDenied: caught=" + caught.toString());
            onAccessDenied();
            return;
        }


        // TODO: show other dialog for other kind of exceptions ?


        onServiceFailure(caught);
    }

    /**
     * User can override this method to handle failures. Default implementation
     * do nothing to not bother client with implementation of stuff that is not
     * needed in most of situations.
     *
     * @param caught
     */
    protected void onServiceFailure(Throwable caught) {
        // empty - Override to implement custom error handling
        throw new RuntimeException(caught);
    }

    protected boolean isNotAuthorized(final SecurityResponseMessage securityResponseMessage) {
        // If not logged, display the logging popup window and stop
        if (securityResponseMessage != null && securityResponseMessage.getStatus() == Response.SC_UNAUTHORIZED) {
            return true;
        }
        return false;
    }

    protected boolean isAccessDenied(final Throwable error) {
        boolean accessDenied = false;
        if (error instanceof StatusCodeException) {
            int statusCode = ((StatusCodeException) error).getStatusCode();
            if (statusCode == Response.SC_FORBIDDEN) {
                accessDenied = true;
            } else {
                ASYNC_CALLBACK_LOGGER.log(Level.WARNING, "Non handled case. Status code received is: " + statusCode);
            }
        }
        return accessDenied;
    }

    /*
     * (non-Javadoc) @see
     * com.gwtsecurity.client.SecurityCallbackHandler#onAuthorizationExpected(java.lang.String)
     */
    @Override
    public void onAuthorizationExpected(final String externalLoginUrl) {
        ASYNC_CALLBACK_LOGGER.log(Level.INFO, "onAuthorizationExpected: externalLoginUrl=" + externalLoginUrl);
        System.err.println("SecuredAsyncCallback . onAuthorizationExpected: externalLoginUrl=" + externalLoginUrl);
        if (externalLoginUrl == null) {
            new LoginDialogBox().show();
        } else { // redirect
            final SecurityDialogBoxes.AlertBox box = new SecurityDialogBoxes.AlertBox(
                    "You are not logged : you will be redirected");
            box.addCloseHandler(new CloseHandler<PopupPanel>() {

                @Override
                public void onClose(CloseEvent<PopupPanel> event) {
                    ClientUtils.redirectRelative(externalLoginUrl);
                }
            });
            box.show();
        }

    }

    /*
     * (non-Javadoc) @see
     * com.gwtsecurity.client.SecurityCallbackHandler#onAccessDenied()
     */
    @Override
    public void onAccessDenied() {
        new AccessDeniedBox().show();
    }
}
