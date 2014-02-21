/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings.interfaces;

import com.eprovement.poptavka.client.common.monitors.ValidationMonitor;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.user.settings.SettingsHandler;
import com.eprovement.poptavka.client.user.settings.widget.SecuritySettingsPresenter;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.view.LazyView;

/**
 * Interface for managing SecuritySettings widget.
 *
 * <h4>Note</h4>
 * Manage Gateway and Presenter in 1:1 relation.
 * Any change to this interface will automatically recquire change in implementation
 * which is good. Easy usage search, which is also very usefull.
 * Also doctypes are available despite calling through eventbus.
 *
 * @author Martin Slavkovsky
 * @since 18.2.2014
 */
public interface ISecuritySettings {

    public interface Gateway {

        /**
         * Inits SecuritySettings widget.
         * @param holder where widget's view will be set
         */
        @Event(handlers = SecuritySettingsPresenter.class)
        void initSecuritySettings(SimplePanel holder);

        /**
         * Sets widget's profile data with given security's data.
         * @param detail object carrying security's profile data
         */
        @Event(handlers = SecuritySettingsPresenter.class)
        void setSecuritySettings(SettingDetail detail);

        /**
         * Requests whether given password match current user's password.
         * @param userId - user whose password is going to be checked
         * @param password - provided password to be checked
         * @param correct - true if given password matches user's current password, false if not
         */
        @Event(handlers = SettingsHandler.class)
        void requestCheckCurrentPassword(long userId, String password);

        /**
         * Notifies user when new and current passwords are the same.
         * @param correct true if same, false otherwise
         */
        @Event(handlers = SecuritySettingsPresenter.class)
        void responseCheckCurrentPassword(boolean correct);

        /**
         * Requests for changing password.
         * Send request to change the password and notify user.
         * @param userId - user whose password is going to be reset
         * @param newPassword - user's new password
         */
        @Event(handlers = SettingsHandler.class)
        void requestResetPassword(long userId, String newPassword);

        /**
         * Notifies user when password has been changed.
         * @param result true if successfully changed, false otherwise
         */
        @Event(handlers = SecuritySettingsPresenter.class)
        void responseResetPassword(boolean result);
    }

    public interface Presenter {

        /**
         * @see Gateway#initSecuritySettings(SimplePanel)
         */
        void onInitSecuritySettings(SimplePanel holder);

        /**
         * @see Gateway#setSecuritySettings(SettingDetail)
         */
        void onSetSecuritySettings(SettingDetail detail);
    }

    public interface View extends LazyView, IsWidget, ProvidesValidate {

        /**
         * @see Gateway#initSecuritySettings(SimplePanel)
         */
        void setSecuritySettings(SettingDetail detail);

        /**
         * Sets password validation monitor styles.
         * @param correct true if valid, false if invalid
         */
        void setCurrentPasswordStyles(boolean correct);

        /**
         * @return the email validation monitor
         */
        ValidationMonitor getEmailMonitor();

        /**
         * @return the current password validation monitor
         */
        ValidationMonitor getPasswordCurrentMonitor();

        /**
         * @return the new password validation monitor
         */
        ValidationMonitor getPasswordNewMonitor();

        /**
         * @return the new password confirm validation monitor
         */
        ValidationMonitor getPasswordNewConfirmMonitor();

        /**
         * @return the change button
         */
        Button getChangeBtn();

        /**
         * Validates if password change form is valid.
         * @return true if valid, false otherwise
         */
        boolean isNewPasswordValid();
    }
}
