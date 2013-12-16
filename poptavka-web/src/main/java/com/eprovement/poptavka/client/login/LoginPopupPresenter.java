/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.login;

import com.eprovement.poptavka.client.common.session.Constants;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import com.eprovement.poptavka.client.common.session.Storage;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ProgressBar;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.mvp4g.client.history.NavigationConfirmationInterface;
import com.mvp4g.client.history.NavigationEventCommand;

/**
 * Login presenter.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = LoginPopupView.class)
public class LoginPopupPresenter extends LazyPresenter<LoginPopupPresenter.LoginPopupInterface, LoginEventBus>
        implements NavigationConfirmationInterface {

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
    public interface LoginPopupInterface extends LazyView {

        void setLoadingMessage(String localizableMessage);

        void setLoadingProgress(Integer newPercentage, String newMessage);

        void setErrorMessage(String message);

        void setInfoMessage(String message);

        //TextBoxes
        TextBox getLogin();

        TextBox getPassword();

        TextBox getForgotPasswordEmail();

        //Progress Bar
        ProgressBar getProgressBar();

        //Buttons
        Button getSubmitBtn();

        Button getCancelBtn();

        Button getResetPasswordButton();

        //Other
        void resetView();

        boolean isValid();

        LoginPopupView getWidget();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private int widgetToLoad = Constants.NONE;

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing by default
    }

    public void onForward() {
        eventBus.showThankYouPopupAfterLogin(false);
        view.resetView();
    }

    @Override
    public void confirm(NavigationEventCommand event) {
        // nothing by default
    }

    /**************************************************************************/
    /* Bind handlers                                                          */
    /**************************************************************************/
    /**
     * Bind login button handlers.
     */
    @Override
    public void bindView() {
        view.getSubmitBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                login(widgetToLoad);
            }
        });
        view.getCancelBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onHideView();
            }
        });
        view.getLogin().addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == Constants.ENTER_KEY_CODE) {
                    login(widgetToLoad);
                }
            }
        });
        view.getPassword().addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == Constants.ENTER_KEY_CODE) {
                    login(widgetToLoad);
                }
            }
        });
        view.getResetPasswordButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.resetPassword(view.getForgotPasswordEmail().getText());
            }
        });
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Show login popup window. Navigation event to access login module.
     * @param widgetToLoad - define widget ID to load after login is finished
     */
    public void onLogin(int widgetToLoad) {
        this.widgetToLoad = widgetToLoad;

        view.getWidget().show();
    }

    /**
     * Login user automatically by inserting given credentials into user & password
     * text boxes and triggering login event.
     *
     * @param email - given user login email
     * @param password - given user login password
     * @param widgetToLoad - widget's ID that is loaded after login finishes
     */
    public void onAutoLogin(String email, String password, int widgetToLoad) {
        view.getLogin().setText(email);
        view.getPassword().setText(password);
        view.setInfoMessage("You are being logging in...");
        view.getSubmitBtn().setEnabled(false);
        login(widgetToLoad);
    }

    /**
     * Sets loading process in progress bar.
     * @param percentage - progress as percentage value
     * @param message of progress
     */
    public void onSetLoadingProgress(int percentage, String message) {
        view.setLoadingProgress(percentage, message);
    }

    /**
     * Sets error message if an error occurs.
     * @param message - error message
     */
    public void onSetErrorMessage(String message) {
        view.setErrorMessage(message);
    }

    /**
     * Hides login popup.
     */
    public void onHideView() {
        view.getWidget().hide();
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Login user. First verify if user's account is activated.
     * Then login user and store him to Storage.
     *
     * @param widgetToLoad - widget's ID that is loaded after login finishes
     */
    private void login(int widgetToLoad) {
        if (view.isValid()) { // both email and password fields are not empty
            view.setLoadingProgress(0, Storage.MSGS.loggingVerifyAccount());
            eventBus.verifyUser(getUserEmail(), getUserPassword(), widgetToLoad);
        } else {
            view.setErrorMessage(Storage.MSGS.commonEmptyCredentials());
        }
    }

    /**
     * Get user's email.
     * @return user's email as trimmed string
     */
    private String getUserEmail() {
        return view.getLogin().getText().trim();
    }

    /**
     * Get user's password.
     * @return user's password
     */
    private String getUserPassword() {
        return view.getPassword().getText();
    }
}
