/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.login.activation;

import com.eprovement.poptavka.client.login.LoginEventBus;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.root.UserActivationResult;
import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.Date;

/**
 * Activation code popup presenter.
 * @author Martin Slavkovsky
 */
@Presenter(view = ActivationCodePopupView.class, multiple = true)
public class ActivationCodePopupPresenter
    extends LazyPresenter<ActivationCodePopupPresenter.ActivationCodePopupInterface, LoginEventBus> {

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** Class attributes. **/
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private int widgetToLoad = Constants.NONE;
    private BusinessUserDetail user;

    /**************************************************************************/
    /* VIEW INTERFACE                                                         */
    /**************************************************************************/
    public interface ActivationCodePopupInterface extends LazyView, ProvidesValidate {

        /** TEXTBOX. **/
        TextBox getActivationCodeBox();

        /** BUTTONS. **/
        Button getCloseButton();

        Button getActivateButton();

        Button getSendAgainButton();

        Button getReportButton();

        /** WIDGET. **/
        Alert getStatus();

        ControlGroup getControlGroup();

        Label getStatusLabel();

        ActivationCodePopupView getWidgetView();
    }

    /**************************************************************************/
    /* BIND                                                                   */
    /**************************************************************************/
    /**
     * Bind activation code buttons handlers.
     */
    @Override
    public void bindView() {
        view.getCloseButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                view.getWidgetView().hide();
                Timer additionalAction = new Timer() {
                    @Override
                    public void run() {
                        eventBus.goToHomeWelcomeModule();
                    }
                };
                eventBus.showThankYouPopup(Storage.MSGS.thankYouActivationClose(), additionalAction);
            }
        });
        view.getActivateButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (view.isValid()) {
                    view.getActivateButton().setEnabled(false);
                    eventBus.activateUser(user, view.getActivationCodeBox().getText());
                } else {
                    view.getControlGroup().setType(ControlGroupType.ERROR);
                    view.getStatus().setType(AlertType.ERROR);
                    view.getStatusLabel().setText("Actication code cannot be empty");
                }
            }
        });
        view.getSendAgainButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                view.getSendAgainButton().setEnabled(false);
                view.getStatusLabel().setText(MSGS.activationNewCodeSending() + user.getEmail() + "...");
                eventBus.sendActivationCodeAgain(user);
            }
        });
        view.getReportButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                view.getWidgetView().hide();
                //presmerovat na root module kde sa toto zavola
                eventBus.sendUsEmail(Constants.SUBJECT_REPORT_ISSUE, (new Date()).toString());
            }
        });
    }

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Initialize activation code popup.
     * @param user that has to be activated/authenticated
     * @param widgetToLoad is a constant of view that will be loaded at the end
     */
    public void onInitActivationCodePopup(BusinessUserDetail user, int widgetToLoad) {
        this.user = user;
        this.widgetToLoad = widgetToLoad;
        view.getStatusLabel().setText(MSGS.activationCodeSent() + " " + user.getEmail());
    }

    /**************************************************************************/
    /* Response methods                                                       */
    /**************************************************************************/
    /**
     * Displays result of activation process.
     * @param activationResult enum of activation process results
     */
    public void onResponseActivateUser(UserActivationResult activationResult) {
        if (activated) {
            view.getStatus().setType(AlertType.SUCCESS);
        } else {
            view.getStatus().setType(AlertType.ERROR);
        }

        //inform user
        switch (activationResult) {
            case OK:
                reportActivationSuccessAndLoginUser();
                break;
            case ERROR_UNKNOWN_USER:
                reportActivationFailure(MSGS.activationFailedUnknownUser());
                break;
            case ERROR_INCORRECT_ACTIVATION_CODE:
                reportActivationFailure(MSGS.activationFailedIncorrectActivationCode());
                break;
            case ERROR_EXPIRED_ACTIVATION_CODE:
                reportActivationFailure(MSGS.activationFailedExpiredActivationCode());
                break;
            default:
                reportActivationFailure(MSGS.activationFailedUnknownError());
        }

    }

    /**
     * Notifies user that new activation code has or has not been successfully sent.
     * @param sent true if resending was successfull, false otherwise
     */
    public void onResponseSendActivationCodeAgain(boolean sent) {
        view.getSendAgainButton().setEnabled(true);
        if (sent) {
            view.getStatus().setType(AlertType.SUCCESS);
        } else {
            view.getStatus().setType(AlertType.ERROR);
        }

        //inform user
        if (sent) {
            view.getStatusLabel().setText(MSGS.activationNewCodeSent() + " " + user.getEmail());
        } else {
            reportActivationFailure(MSGS.activationFailedSentNewCode());
        }
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Notifies user that activation was successfull and log him.
     */
    private void reportActivationSuccessAndLoginUser() {
        view.getStatusLabel().setText(MSGS.activationPassed());
        view.getStatus().setType(AlertType.SUCCESS);
        //close activation popup
        view.getWidgetView().hide();
        //login user automatically
        eventBus.showThankYouPopupAfterLogin(true);
        eventBus.autoLogin(
            user.getEmail(),
            user.getPassword(),
            widgetToLoad);
    }

    /**
     * Notifies user that activation was NOT successfull.
     * @param errorMessage - error message
     */
    private void reportActivationFailure(String errorMessage) {
        view.getStatusLabel().setText(errorMessage);
        view.getStatus().setType(AlertType.ERROR);
        view.getActivateButton().setEnabled(true);
        view.getReportButton().setVisible(true);
    }
}
