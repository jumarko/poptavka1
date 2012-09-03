package com.eprovement.poptavka.client.common.security;

import com.eprovement.poptavka.shared.exceptions.ExceptionUtils;
import com.eprovement.poptavka.shared.exceptions.SecurityDialogBoxes.AccessDeniedBox;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.mvp4g.client.event.EventBusWithLookup;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A new AsyncCallback that can handle security responses.
 *
 * @author dmartin
 */
public abstract class SecuredAsyncCallback<T> implements AsyncCallback<T>, SecurityCallbackHandler {

    protected static final Logger ASYNC_CALLBACK_LOGGER = Logger.getLogger("SecuredAsyncCallback");
    private EventBusWithLookup lookupEventbus;

    public SecuredAsyncCallback(EventBusWithLookup lookupEventbus) {
        this.lookupEventbus = lookupEventbus;
    }

    /**
     * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
     */
    @Override
    public final void onFailure(final Throwable caught) {
        // If not authorized display LoginPopupWindow
        if (isNotAuthorized(caught)) {
            ASYNC_CALLBACK_LOGGER.log(Level.WARNING, "isNotAuthrized: message status=" + caught.toString());
            onAuthorizationExpected(null); // instead of null, send message to user that he is not authorized.
            return;
        }

        // If access is denied, display an error message box (AccessDeniedBox)
        if (isAccessDenied(caught)) {
            ASYNC_CALLBACK_LOGGER.log(Level.WARNING, "isAccessDenied: caught=" + caught.toString());
            onAccessDenied();
            return;
        }

        // TODO: show other dialog for other kind of exceptions ?
        onServiceFailure(caught);
    }

    /**
     * User can override this method to handle failures. Default implementation do nothing to not bother client with
     * implementation of stuff that is not needed in most of situations.
     *
     * @param caught
     */
    protected void onServiceFailure(Throwable caught) {
        // TODO IV why the hell you removed my dialog box ?! ]:->
        // TODO: more reasonable message must be provided -> probably reference to error id with possibility to send
        // message to our customer support team.
        // error id should be generated in RpcExceptionAspect
        new com.eprovement.poptavka.shared.exceptions.SecurityDialogBoxes.AlertBox(
                ExceptionUtils.getFullErrorMessage(caught)).show();

    }

    protected boolean isNotAuthorized(final Throwable error) {
        if (error instanceof StatusCodeException
                && ((StatusCodeException) error).getStatusCode() == Response.SC_UNAUTHORIZED) {
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
        new AccessDeniedBox().show();
    }
}
