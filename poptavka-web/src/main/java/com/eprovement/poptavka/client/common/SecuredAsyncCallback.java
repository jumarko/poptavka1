/**
 *
 */
package com.eprovement.poptavka.client.common;

import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.SecurityDialogBoxes;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A new AsyncCallback that can handle security responses.
 *
 * @author dmartin
 */
public abstract class SecuredAsyncCallback<T> implements AsyncCallback<T> {

    protected static final Logger ASYNC_CALLBACK_LOGGER = Logger.getLogger("SecuredAsyncCallback");


    /**
     * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
     */
    @Override
    public final void onFailure(final Throwable caught) {

        ASYNC_CALLBACK_LOGGER.log(Level.INFO, "Failure in AsyncCallback", caught);
        if (caught == null) {
            ASYNC_CALLBACK_LOGGER.log(Level.FINEST, "Received parameter 'caught' is null ?!!");
            return;
        } else if (caught instanceof ApplicationSecurityException) {

            new SecurityDialogBoxes.AccessDeniedBox().show();
//            ExceptionUtils.showErrorDialog("Authorization required",
//                    "You cannot access this funcionality.<br />You are logged as user, who does not support this");
            return;
        }
        // TODO: show other dialog for other kind of exceptions ?
        // e.g.
        // if (caught instanceof RPCException) {
//        RPCException commonException = (RPCException) caught;
//        errorDialog = new ErrorDialogPopupView();
//        errorDialog.show(commonException.getSymbol());
//    }

        onServiceFailure(caught);
    }

    /**
     * User can override this method to handle failures. Default implementation do nothing to not bother
     * client with implementation of stuff that is not needed in most of situations.
     *
     * @param caught
     */
    protected void onServiceFailure(Throwable caught) {
        // empty - Override to implement custom error handling
        throw new RuntimeException(caught);
    }

}
