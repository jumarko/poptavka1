/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.user.settings.interfaces.ISystemSettings;
import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.shared.domain.settings.NotificationDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.github.gwtbootstrap.client.ui.FluidContainer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * View consists of table of notifications, each represented by NotificationItem widget.
 *
 * @author Martin Slavkovsky
 */
public class SystemSettingsView extends Composite implements ISystemSettings.View {

    /**************************************************************************/
    /* UIBINDER                                                               */
    /**************************************************************************/
    private static UserSettingsViewUiBinder uiBinder = GWT.create(UserSettingsViewUiBinder.class);

    interface UserSettingsViewUiBinder extends UiBinder<Widget, SystemSettingsView> {
    }

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField FluidContainer fluidContainer;
    @UiField FlowPanel notifications;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    /**
     * Creates SystemSettings view's compontents.
     */
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public void setSystemSettings(SettingDetail detail) {
        //notifications
        notifications.clear();
        for (NotificationDetail item : detail.getNotifications()) {
            notifications.add(new NotificationItemView(item));
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void fillSystemSettings(SettingDetail detail) {
        //notifications
        for (int i = 0; i < detail.getNotifications().size(); i++) {
            NotificationItemView notificationWidget = (NotificationItemView) notifications.getWidget(i);
            NotificationDetail notificationDetail = detail.getNotifications().get(i);

            notificationDetail.setEnabled(notificationWidget.getEnabled());
            notificationDetail.setName(notificationWidget.getName().getText());
            notificationDetail.setPeriod(Period.valueOf(notificationWidget.getPeriod()));
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public boolean isValid() {
        return true;
    }
}
