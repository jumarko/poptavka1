/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings.interfaces;

import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.user.settings.widget.ClientSettingsPresenter;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.view.LazyView;

/**
 * Interface for managing ClientSettings widget.
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
public interface IClientSettings {

    public interface Gateway {

        /**
         * Inits ClientSettings widget.
         * @param holder where widget's view will be set
         */
        @Event(handlers = ClientSettingsPresenter.class)
        void initClientSettings(SimplePanel holder);

        /**
         * Sets widget's profile data with given client's data.
         * @param detail object carrying client's profile data
         */
        @Event(handlers = ClientSettingsPresenter.class)
        void setClientSettings(SettingDetail detail);

        /**
         * Fills given client's profile data with current widget's data.
         * @param detail to be updated
         * @return updated detail object
         */
        @Event(handlers = ClientSettingsPresenter.class, passive = true)
        void fillClientSettings(SettingDetail detail);
    }

    public interface Presenter {

        /**
         * @see Gateway#initClientSettings(SimplePanel)
         */
        void onInitClientSettings(SimplePanel holder);

        /**
         * @see Gateway#setClientSettings(SettingDetail)
         */
        void onSetClientSettings(SettingDetail detail);

        /**
         * @see Gateway#fillClientSettings(SettingDetail)
         */
        void onFillClientSettings(SettingDetail detail);
    }

    public interface View extends LazyView, ProvidesValidate, IsWidget {

        /**
         * @see Gateway#initClientSettings(SimplePanel)
         */
        void setClientSettings(SettingDetail detail);

        /**
         * @see Gateway#fillClientSettings(SettingDetail)
         */
        void fillClientSettings(SettingDetail detail);
    }
}
