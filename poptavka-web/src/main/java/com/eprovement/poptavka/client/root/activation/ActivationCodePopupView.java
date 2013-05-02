package com.eprovement.poptavka.client.root.activation;

import com.github.gwtbootstrap.client.ui.Alert;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author Martin Slavkovsky
 */
public class ActivationCodePopupView extends PopupPanel
        implements ActivationCodePopupPresenter.ActivationCodePopupInterface {

    /**************************************************************************/
    /* UIBINDER                                                               */
    /**************************************************************************/
    private static ActivationCodePopupView.ActivationCodePopupUiBinder uiBinder =
            GWT.create(ActivationCodePopupView.ActivationCodePopupUiBinder.class);

    interface ActivationCodePopupUiBinder extends UiBinder<Widget, ActivationCodePopupView> {
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField Alert status;
    @UiField TextBox activationCodeBox;
    @UiField Button closeButton, activateButton, sendAgainButton, reportButton;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        setWidget(uiBinder.createAndBindUi(this));

        activationCodeBox.setFocus(true);

        setModal(true);
        setGlassEnabled(true);
        center();
        show();
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /** TEXTBOX. **/
    @Override
    public TextBox getActivationCodeBox() {
        return activationCodeBox;
    }

    /** BUTTONS. **/
    @Override
    public Button getCloseButton() {
        return closeButton;
    }

    @Override
    public Button getActivateButton() {
        return activateButton;
    }

    @Override
    public Button getSendAgainButton() {
        return sendAgainButton;
    }

    @Override
    public Button getReportButton() {
        return reportButton;
    }

    /** WIDGET. **/
    @Override
    public Alert getStatus() {
        return status;
    }

    @Override
    public ActivationCodePopupView getWidgetView() {
        return this;
    }
}
