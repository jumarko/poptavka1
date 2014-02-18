/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings.interfaces;

import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.user.settings.widget.SystemSettingsPresenter;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.view.LazyView;

/**
 * Interface for managing SystemSettings widget.
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
public interface ISystemSettings {

    public interface Gateway {

        /**
         * Inits SystemSettings widget.
         * @param holder where widget's view will be set
         */
        @Event(handlers = SystemSettingsPresenter.class)
        void initSystemSettings(SimplePanel holder);

        /**
         * Sets widget's profile data with given system's data.
         * @param detail object carrying system's profile data
         */
        @Event(handlers = SystemSettingsPresenter.class)
        void setSystemSettings(SettingDetail detail);

        /**
         * Fills given system's profile data with current widget's data.
         * @param detail to be updated
         * @return updated detail object
         */
        @Event(handlers = SystemSettingsPresenter.class, passive = true)
        void fillSystemSettings(SettingDetail detail);
    }

    public interface Presenter {

        /**
         * @see Gateway#initSystemSettings(SimplePanel)
         */
        void onInitSystemSettings(SimplePanel holder);

        /**
         * @see Gateway#setSystemSettings(SettingDetail)
         */
        void onSetSystemSettings(SettingDetail detail);

        /**
         * @see Gateway#fillSystemSettings(SettingDetail)
         */
        void onFillSystemSettings(SettingDetail detail);
    }

    public interface View extends LazyView, ProvidesValidate, IsWidget {

        /**
         * @see Gateway#initSystemSettings(SimplePanel)
         */
        void setSystemSettings(SettingDetail detail);

        /**
         * @see Gateway#setSystemSettings(SettingDetail)
         */
        void fillSystemSettings(SettingDetail detail);
    }
}
