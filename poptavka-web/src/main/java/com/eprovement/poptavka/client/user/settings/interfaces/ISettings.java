/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings.interfaces;

import com.eprovement.poptavka.client.root.toolbar.ProvidesToolbar;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.history.NavigationConfirmationInterface;
import com.mvp4g.client.view.LazyView;

/**
 *
 * @author Martin Slavkovsky
 * @since 18.2.2014
 */
public interface ISettings {

    public enum SettingsTab {

        USER,
        CLIENT,
        SUPPLIER,
        SYSTEM,
        SECURITY;
    }

    public interface Gateway {

        /**
         * Inits settings module.
         * @return history token string
         */
        @Event(forwardToParent = true)
        String goToSettingsModule();
    }

    public interface Presenter extends NavigationConfirmationInterface {

    }

    public interface View extends LazyView, IsWidget, ProvidesToolbar {

        /** Setters. **/
        void allowSupplierSettings(boolean visible);

        /**
         * Sets active style for given tab.
         */
        void settingsTabStyleChange(SettingsTab tab);

        /**
         * @return the content container
         */
        SimplePanel getContentPanel();

        /**
         * @return the footer container
         */
        SimplePanel getFooterContainer();

        /**
         * @return the tab button
         */
        Button getTabBtn(SettingsTab tab);
    }

}
