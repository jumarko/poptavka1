/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.user.settings.SettingsEventBus;
import com.eprovement.poptavka.client.user.settings.interfaces.ISystemSettings;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;

/**
 * SystemSettings widget is part of Settings module widgets.
 * Displays system settings.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = SystemSettingsView.class)
public class SystemSettingsPresenter extends LazyPresenter<ISystemSettings.View, SettingsEventBus>
    implements ISystemSettings.Presenter {

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public void onInitSystemSettings(SimplePanel holder) {
        holder.setWidget(view);
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public void onSetSystemSettings(SettingDetail detail) {
        view.setSystemSettings(detail);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void onFillSystemSettings(SettingDetail detail) {
        view.fillSystemSettings(detail);
    }
}
