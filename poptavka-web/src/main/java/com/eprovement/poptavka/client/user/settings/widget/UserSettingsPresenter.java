/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.user.settings.SettingsEventBus;
import com.eprovement.poptavka.client.user.settings.interfaces.IUserSettings;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;

/**
 * UserSettings widget is part of Settings module widgets.
 * Displays common user's profile data.
 *
 * @author Martin Slavkovsky
 * @since 18.2.2014
 */
@Presenter(view = UserSettingsView.class)
public class UserSettingsPresenter extends LazyPresenter<IUserSettings.View, SettingsEventBus>
    implements IUserSettings.Presenter {

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * {@inheritDoc}
     */
    @Override
    public void onInitUserSettings(SimplePanel holder) {
        holder.setWidget(view);
        eventBus.initAddressSelector(view.getAddressHolder());
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetUserSettings(SettingDetail detail) {
        eventBus.setAddresses(detail.getUser().getAddresses());
        view.setUserSettings(detail);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFillUserSettings(SettingDetail detail) {
        view.fillUserSettings(detail);
        eventBus.fillAddresses(detail.getUser().getAddresses());
    }
}