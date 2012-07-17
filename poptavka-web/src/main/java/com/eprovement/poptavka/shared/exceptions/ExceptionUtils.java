package com.eprovement.poptavka.shared.exceptions;

import com.eprovement.poptavka.client.common.errorDialog.ErrorDialogPopupView;

public final class ExceptionUtils {

    private ExceptionUtils() {
        super();
    }

    public static void showErrorDialog(ErrorDialogPopupView errorDialog, Throwable caught) {
        RPCException commonException = (RPCException) caught;
        errorDialog = new ErrorDialogPopupView();
        errorDialog.show(commonException.getSymbol());
    }
}
