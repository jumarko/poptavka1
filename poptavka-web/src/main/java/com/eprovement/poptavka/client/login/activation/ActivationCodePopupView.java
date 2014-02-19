/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.login.activation;

import com.eprovement.poptavka.resources.StyleResource;
import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.github.gwtbootstrap.client.ui.constants.BackdropType;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Activation code popup is a bootstrap modal popup.
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
    /**
     * @return the activation cote textbox
     */
    @Override
    public TextBox getActivationCodeBox() {
        return activationCodeBox;
    }

    /** BUTTONS. **/
    /**
     * @return the close button
     */
    @Override
    public Button getCloseButton() {
        return closeButton;
    }

    /**
     * @return the activate button
     */
    @Override
    public Button getActivateButton() {
        return activateButton;
    }

    /**
     * @return the send again button
     */
    @Override
    public Button getSendAgainButton() {
        return sendAgainButton;
    }

    /**
     * @return the report button
     */
    @Override
    public Button getReportButton() {
        return reportButton;
    }

    /** WIDGET. **/
    /**
     * @return the status alert
     */
    @Override
    public Alert getStatus() {
        return status;
    }

    /**
     * @return the control group
     */
    @Override
    public ControlGroup getControlGroup() {
        return controlGroup;
    }

    /**
     * @return the status label
     */
    @Override
    public Label getStatusLabel() {
        return statusLabel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        controlGroup.setType(ControlGroupType.INFO);
        status.setType(AlertType.INFO);
        statusLabel.setText("Please insert your activation code");
    }

    /**
     * Validate view components.
     * @return true if view is valid, false otherwise
     */
    @Override
    public boolean isValid() {
        return !activationCodeBox.getText().isEmpty();
    }

    /**
     * @return the widget view
     */
    @Override
    public ActivationCodePopupView getWidgetView() {
        return this;
    }
}