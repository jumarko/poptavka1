/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings.interfaces;

import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.user.settings.widget.UserSettingsPresenter;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.view.LazyView;

/**
 * Interface for managing UserSettings widget.
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
public interface IUserSettings {

    public interface Gateway {

        /**
         * Inits UserSettings widget.
         * @param holder where widget's view will be set
         */
        @Event(handlers = UserSettingsPresenter.class)
        void initUserSettings(SimplePanel holder);

        /**
         * Sets widget's profile data with given user's data.
         * @param detail object carrying user's profile data
         */
        @Event(handlers = UserSettingsPresenter.class)
        void setUserSettings(SettingDetail detail);

        /**
         * Fills given user's profile data with current widget's data.
         * @param detail to be updated
         * @return updated detail object
         */
        @Event(handlers = UserSettingsPresenter.class, passive = true)
        void fillUserSettings(SettingDetail detail);
    }

    public interface Presenter {

        /**
         * @see Gateway#initUserSettings(SimplePanel)
         */
        void onInitUserSettings(SimplePanel holder);

        /**
         * @see Gateway#setUserSettings(SettingDetail)
         */
        void onSetUserSettings(SettingDetail detail);

        /**
         * @see Gateway#fillUserSettings(SettingDetail)
         */
        void onFillUserSettings(SettingDetail detail);
    }

    public interface View extends LazyView, ProvidesValidate, IsWidget {

        /**
         * @see Gateway#initUserSettings(SimplePanel)
         */
        void setUserSettings(SettingDetail detail);

        /**
         * @see Gateway#setUserSettings(SettingDetail)
         */
        void fillUserSettings(SettingDetail detail);

        /**
         * @return the address container.
         */
        SimplePanel getAddressHolder();
    }
}
