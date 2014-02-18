/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.user.settings.SettingsEventBus;
import com.eprovement.poptavka.client.user.settings.interfaces.IClientSettings;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;

/**
 * Client settings widget is part of Settings module's widgets.
 * Displays client's profile data only.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = ClientSettingsView.class)
public class ClientSettingsPresenter extends LazyPresenter<IClientSettings.View, SettingsEventBus>
    implements IClientSettings.Presenter {

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * {@inheritDoc}
     */
    @Override
    public void onInitClientSettings(SimplePanel holder) {
        holder.setWidget(view);
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetClientSettings(SettingDetail detail) {
        view.setClientSettings(detail);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFillClientSettings(SettingDetail detail) {
        view.fillClientSettings(detail);
    }
}
