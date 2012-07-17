package com.eprovement.poptavka.client.common.errorDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;

/**
 * This class represents dialog box, shown when some error occurs during RPC
 * call.
 *
 * @author kolkar
 *
 */
public class ErrorDialogPopupView extends Composite {

    interface ResponseDialogUiBinder extends
            UiBinder<DialogBox, ErrorDialogPopupView> {
    }

    @UiField
    Button okButton;

    @UiField
    HTML errorText;

    @UiField
    DialogBox dialogBox;

    public ErrorDialogPopupView() {
        ResponseDialogUiBinder uiBinder = GWT
                .create(ResponseDialogUiBinder.class);
        uiBinder.createAndBindUi(this);
        dialogBox.center();
        dialogBox.hide();
    }

    public void show(String responseText) {
        errorText.setHTML(responseText);
        dialogBox.show();
    }

    @UiHandler("okButton")
    void onClick(ClickEvent e) {
        dialogBox.hide();
    }
}
