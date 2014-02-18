/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.settings.SettingsEventBus;
import com.eprovement.poptavka.client.user.settings.interfaces.ISecuritySettings;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;

/**
 * SecuritySettings widget is part of Settings module's widgets.
 * Displays security settings.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = SecuritySettingsView.class)
public class SecuritySettingsPresenter extends LazyPresenter<ISecuritySettings.View, SettingsEventBus>
    implements ISecuritySettings.Presenter {

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    /**
     * {@inheritDoc}
     */
    @Override
    public void onInitSecuritySettings(SimplePanel holder) {
        holder.setWidget(view);
    }

    /**************************************************************************/
    /* BIND                                                                   */
    /**************************************************************************/
    /**
     * Binds validation handlers and change button handler.
     */
    @Override
    public void bindView() {
        view.getChangeBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (view.isNewPasswordValid()) {
                    eventBus.requestCheckCurrentPassword(Storage.getUser().getUserId(),
                        (String) view.getPasswordCurrentMonitor().getValue());
                }
            }
        });
        /** Focus. **/
        ((TextBox) view.getPasswordCurrentMonitor().getWidget()).addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                view.getPasswordCurrentMonitor().resetValidation();
                ((TextBox) view.getPasswordCurrentMonitor().getWidget()).setText("");
            }
        });
        ((TextBox) view.getPasswordNewMonitor().getWidget()).addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                view.getPasswordNewMonitor().resetValidation();
                ((TextBox) view.getPasswordNewMonitor().getWidget()).setText("");
            }
        });
        ((TextBox) view.getPasswordNewConfirmMonitor().getWidget()).addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                view.getPasswordNewConfirmMonitor().resetValidation();
                ((TextBox) view.getPasswordNewConfirmMonitor().getWidget()).setText("");
            }
        });
        /** Key up events. **/
        ((TextBox) view.getPasswordNewMonitor().getWidget()).addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                initVisualPasswordCheck();
            }
        });
        ((TextBox) view.getPasswordNewConfirmMonitor().getWidget()).addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                initVisualPasswordConfirmCheck();
            }
        });
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetSecuritySettings(SettingDetail detail) {
        view.setSecuritySettings(detail);
    }

    /**
     * Notifies user when password has been changed.
     * @param result true if successfully changed, false otherwise
     */
    public void onResponseResetPassword(boolean result) {
        if (result) {
            Window.alert(Storage.MSGS.userSettingsPasswordChangedSucceeded());
            view.setDefaultPasswordsStyles();
        } else {
            Window.alert(Storage.MSGS.userSettingsPasswordChangedFailed());
        }
    }

    /**
     * Notifies user when new and current passwords are the same.
     * @param correct true if same, false otherwise
     */
    public void onResponseCheckCurrentPassword(boolean correct) {
        view.setCurrentPasswordStyles(correct);
        if (correct) {
            eventBus.requestResetPassword(Storage.getUser().getUserId(),
                (String) view.getPasswordNewMonitor().getValue());
        }
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Validates password field.
     */
    private void initVisualPasswordCheck() {
        int passwordLength = ((String) view.getPasswordNewMonitor().getValue()).length();
        if ((passwordLength <= Constants.LONG_PASSWORD) && (passwordLength > Constants.SHORT_PASSWORD)) {
            view.getPasswordNewMonitor().setExternalValidation(
                ControlGroupType.WARNING, Storage.MSGS.formUserRegSemiStrongPassword());
        }
        if (passwordLength > Constants.LONG_PASSWORD) {
            view.getPasswordNewMonitor().setExternalValidation(
                ControlGroupType.SUCCESS, Storage.MSGS.formUserRegStrongPassword());
        }
    }

    /**
     * Validates password confirm field.
     */
    private void initVisualPasswordConfirmCheck() {
        if (!(view.getPasswordNewMonitor().getValue()).equals(view.getPasswordNewConfirmMonitor().getValue())) {
            view.getPasswordNewConfirmMonitor().setExternalValidation(
                ControlGroupType.ERROR, Storage.MSGS.formUserRegPasswordsUnmatch());
        } else {
            view.getPasswordNewConfirmMonitor().setExternalValidation(
                ControlGroupType.SUCCESS, Storage.MSGS.formUserRegPasswordsMatch());
        }
    }
}
