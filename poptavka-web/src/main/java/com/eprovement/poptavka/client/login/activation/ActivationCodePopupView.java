package com.eprovement.poptavka.client.login.activation;

import com.eprovement.poptavka.resources.StyleResource;
import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.BackdropType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author Martin Slavkovsky
 */
public class ActivationCodePopupView extends Modal
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
    @UiField ControlGroup controlGroup;
    @UiField Label statusLabel;
    @UiField TextBox activationCodeBox;
    @UiField Button closeButton, activateButton, sendAgainButton, reportButton;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        add(uiBinder.createAndBindUi(this));

        activationCodeBox.setFocus(true);

        addStyleName(StyleResource.INSTANCE.modal().commonModalStyle());
        addStyleName(StyleResource.INSTANCE.modal().activationCodePopupStyle());
        setBackdrop(BackdropType.STATIC);
        setKeyboard(false);
        setAnimation(true);
        setDynamicSafe(true);
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
    public ControlGroup getControlGroup() {
        return controlGroup;
    }

    @Override
    public Label getStatusLabel() {
        return statusLabel;
    }

    @Override
    public boolean isValid() {
        return !activationCodeBox.getText().isEmpty();
    }

    @Override
    public ActivationCodePopupView getWidgetView() {
        return this;
    }
}