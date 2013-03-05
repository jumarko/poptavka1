/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.shared.domain.settings.NotificationDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
public class SystemSettingsView extends Composite implements SystemSettingsPresenter.SystemSettingsViewInterface {

    /**************************************************************************/
    /* UIBINDER                                                               */
    /**************************************************************************/
    private static SystemSettingsView.UserSettingsViewUiBinder uiBinder = GWT
            .create(SystemSettingsView.UserSettingsViewUiBinder.class);

    interface UserSettingsViewUiBinder extends
            UiBinder<Widget, SystemSettingsView> {
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField
    VerticalPanel notifications;
    /** Class attributes. **/
    private ClickHandler clickHandler;
    private ChangeHandler changeHandler;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Change monitoring methods                                              */
    /**************************************************************************/
    @Override
    public void commit() {
        for (int i = 0; i < notifications.getWidgetCount(); i++) {
            NotificationItemView notificationWidget = (NotificationItemView) notifications.getWidget(i);
            notificationWidget.commit();
        }
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void setSystemSettings(SettingDetail detail) {
        //notifications
        List<Period> periodList = Arrays.asList(Period.values());
        for (NotificationDetail item : detail.getNotifications()) {
            NotificationItemView notificationWidget = new NotificationItemView();
            notificationWidget.getName().setText(item.getName());
            notificationWidget.setEnabledBothValues(item.isEnabled());
            notificationWidget.setPerioddBothValues(periodList.indexOf(item.getPeriod()));
            notificationWidget.addChangeHandler(changeHandler);
            notifications.add(notificationWidget);
        }
    }

    @Override
    public SettingDetail updateSystemSettings(SettingDetail detail) {
        //notifications
        for (int i = 0; i < detail.getNotifications().size(); i++) {
            NotificationItemView notificationWidget = (NotificationItemView) notifications.getWidget(i);
            NotificationDetail notificationDetail = detail.getNotifications().get(i);

            notificationDetail.setEnabled(notificationWidget.getEnabled());
            notificationDetail.setName(notificationWidget.getName().getText());
            notificationDetail.setPeriod(Period.values()[notificationWidget.getPeriod()]);
        }

        return detail;
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void setChangeHandler(ChangeHandler changeHandler) {
        this.changeHandler = changeHandler;
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    @Override
    public Widget getWidgetView() {
        return this;
    }
}
