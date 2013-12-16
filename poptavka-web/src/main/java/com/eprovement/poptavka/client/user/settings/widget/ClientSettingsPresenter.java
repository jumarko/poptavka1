/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.user.settings.SettingsEventBus;
import com.eprovement.poptavka.client.user.settings.widget.ClientSettingsPresenter.ClientSettingsViewInterface;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

/**
 * Client settings widget is part of Settings module's widgets.
 * Displays client's profile data only.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = ClientSettingsView.class, multiple = true)
public class ClientSettingsPresenter extends LazyPresenter<ClientSettingsViewInterface, SettingsEventBus> {

    /**************************************************************************/
    /* VIEW INTERFACE                                                         */
    /**************************************************************************/
    public interface ClientSettingsViewInterface extends LazyView {

        void setClientSettings(SettingDetail detail);

        SettingDetail updateClientSettings(SettingDetail detail);

        boolean isValid();

        Widget getWidgetView();
    }

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    /**
     * Inits ClientSettings widget.
     * @param holder panel
     */
    public void iniClientSettings(SimplePanel holder) {
        holder.setWidget(view.getWidgetView());
    }

    /**************************************************************************/
    /* METHODS                                                                */
    /**************************************************************************/
    /**
     * Sets client's profile data.
     * @param detail carring profile data
     */
    public void onSetClientSettings(SettingDetail detail) {
        view.setClientSettings(detail);
    }

    /**
     * Updates client's profile data of given object with actual widget's profile data.
     * @param detail to be updated
     * @return updated detail object
     */
    public SettingDetail updateClientSettings(SettingDetail detail) {
        return view.updateClientSettings(detail);
    }
}
