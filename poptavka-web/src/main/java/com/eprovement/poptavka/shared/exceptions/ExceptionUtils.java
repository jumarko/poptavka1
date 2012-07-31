package com.eprovement.poptavka.shared.exceptions;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

public final class ExceptionUtils {

    private ExceptionUtils() {
        super();
    }

    public static void showErrorDialog(String errorTitle, String errorMessage) {


        // TODO: show more handy popup
        final PopupPanel errorPanel = new PopupPanel(true, true);
        errorPanel.setWidget(new HTML(errorMessage));
        errorPanel.center();
        errorPanel.show();

    }

}
