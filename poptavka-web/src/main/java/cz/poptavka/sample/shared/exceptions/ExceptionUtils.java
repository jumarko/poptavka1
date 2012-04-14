package cz.poptavka.sample.shared.exceptions;

import cz.poptavka.sample.client.main.errorDialog.ErrorDialogPopupView;

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
