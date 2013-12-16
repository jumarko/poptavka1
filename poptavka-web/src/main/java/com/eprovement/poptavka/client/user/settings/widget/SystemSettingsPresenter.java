/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.user.settings.SettingsEventBus;
import com.eprovement.poptavka.client.user.settings.widget.SystemSettingsPresenter.SystemSettingsViewInterface;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

/**
 * SystemSettings widget is part of Settings module widgets.
 * Displays system settings.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = SystemSettingsView.class, multiple = true)
public class SystemSettingsPresenter extends LazyPresenter<SystemSettingsViewInterface, SettingsEventBus> {

    /**************************************************************************/
    /* VIEW INTERFACE                                                         */
    /**************************************************************************/
    public interface SystemSettingsViewInterface extends LazyView {

        void setSystemSettings(SettingDetail detail);

        SettingDetail updateSystemSettings(SettingDetail detail);

        boolean isValid();

        Widget getWidgetView();
    }

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    /**
     * Creates SystemSettings widget.
     */
    public void initSystemSettings(SimplePanel holder) {
        holder.setWidget(view.getWidgetView());
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Sets system's profile data.
     * @param detail object carrying system's profile data
     */
    public void onSetSystemSettings(SettingDetail detail) {
        view.setSystemSettings(detail);
    }

    /**
     * Updates system's profile data of given object for current widget's data.
     * @param detail to be updated
     * @return updated detail object
     */
    public SettingDetail updateSystemSettings(SettingDetail detail) {
        return view.updateSystemSettings(detail);
    }
}
