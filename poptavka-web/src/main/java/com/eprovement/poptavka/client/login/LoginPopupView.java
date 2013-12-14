/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.login;

import com.eprovement.poptavka.client.login.LoginPopupPresenter.LoginPopupInterface;
import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.resources.StyleResource;
import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.ProgressBar;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.github.gwtbootstrap.client.ui.constants.BackdropType;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Login popup view is a bootstrap modal popup.
 *
 * @author Martin Slavkovsky
 */
public class LoginPopupView extends Modal implements LoginPopupInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private LoginPopupViewUiBinder uiBinder = GWT.create(LoginPopupViewUiBinder.class);

    interface LoginPopupViewUiBinder extends UiBinder<Widget, LoginPopupView> {
    }

    /**************************************************************************/
    /* CSS                                                                    */
    /**************************************************************************/
    static {
        CssInjector.INSTANCE.ensureModalStylesInjected();
        CssInjector.INSTANCE.ensureCommonStylesInjected();
    }

    /**************************************************************************/
    /* Attribute                                                              */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField TextBox emailTextBox, forgotPasswordEmail;
    @UiField PasswordTextBox passwordTextBox;
    @UiField Alert status;
    @UiField ProgressBar progressBar;
    @UiField Label infoLabel;
    @UiField Button submitBtn, cancelBtn, resetPasswordButton;
    @UiField ControlGroup emailControlGroup, passwordControlGroup;
    @UiField DisclosurePanel forgotPassword;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates login view's components
     */
    @Override
    public void createView() {
        add(uiBinder.createAndBindUi(this));

        addStyleName(StyleResource.INSTANCE.modal().commonModalStyle());
        addStyleName(StyleResource.INSTANCE.modal().loginModal());
        setBackdrop(BackdropType.STATIC);
        setAnimation(true);
        setDynamicSafe(true);
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    /**
     * Method displays the current status in the login process.
     *
     * @param message - message to be displayed in LoginPopupView
     */
    @Override
    public void setLoadingMessage(String message) {
        emailControlGroup.setType(ControlGroupType.NONE);
        passwordControlGroup.setType(ControlGroupType.NONE);
        status.setType(AlertType.SUCCESS);
        progressBar.setVisible(true);
        infoLabel.setText(message);
        cancelBtn.setEnabled(false);
        submitBtn.setEnabled(false);
    }

    @Override
    public void setLoadingProgress(Integer newPercentage, String newMessage) {
        if (newPercentage != null) {
            progressBar.setVisible(true);
            progressBar.setPercent(newPercentage);
        }
        if (newMessage != null) {
            if (newPercentage != null && newPercentage == 100) {
                setInfoMessage(newMessage);
            } else {
                setLoadingMessage(newMessage);
            }
        }
    }

    /**
     * Displays <b>error</b> status when some <b>problem</b> occurs.
     * @param message - error message
     */
    @Override
    public void setErrorMessage(String message) {
        emailControlGroup.setType(ControlGroupType.ERROR);
        passwordControlGroup.setType(ControlGroupType.ERROR);
        status.setType(AlertType.ERROR);
        progressBar.setVisible(false);
        infoLabel.setText(message);
        cancelBtn.setEnabled(true);
        submitBtn.setEnabled(true);
    }

    /**
     * Displays <b>info</b> status when some <b>status change</b> occurs.
     * @param message - info message
     */
    @Override
    public void setInfoMessage(String message) {
        emailControlGroup.setType(ControlGroupType.NONE);
        passwordControlGroup.setType(ControlGroupType.NONE);
        status.setType(AlertType.SUCCESS);
        progressBar.setVisible(false);
        infoLabel.setText(message);
        cancelBtn.setEnabled(true);
        submitBtn.setEnabled(true);
    }

    /**
     * Reset view by clearing textboxes and info messages.
     */
    @Override
    public void resetView() {
        emailTextBox.setText("");
        emailTextBox.setFocus(true);
        passwordTextBox.setText("");
        forgotPasswordEmail.setText("");
        setInfoMessage("Enter email and password to login.");
        forgotPassword.setOpen(false);
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    //TextBoxes
    /**
     * @return the password textbox
     */
    @Override
    public TextBox getPassword() {
        return passwordTextBox;
    }

    /**
     * @return the login textbox
     */
    @Override
    public TextBox getLogin() {
        return emailTextBox;
    }

    /**
     * @return the forgot password email textbox
     */
    @Override
    public TextBox getForgotPasswordEmail() {
        return forgotPasswordEmail;
    }

    //ProgressBar
    /**
     * @return the progress bar
     */
    @Override
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    //Buttons
    /**
     * @return the submit button
     */
    @Override
    public Button getSubmitBtn() {
        return submitBtn;
    }

    /**
     * @return the cancel button
     */
    @Override
    public Button getCancelBtn() {
        return cancelBtn;
    }

    /**
     * @return the reset password button
     */
    @Override
    public Button getResetPasswordButton() {
        return resetPasswordButton;
    }

    /**
     * Validate view components.
     * @return true if view is valid, false otherwise
     */
    @Override
    public boolean isValid() {
        return !getLogin().getText().isEmpty() && !getPassword().getText().isEmpty();
    }

    /**
     * @return the widget view
     */
    @Override
    public LoginPopupView getWidget() {
        return this;
    }
}