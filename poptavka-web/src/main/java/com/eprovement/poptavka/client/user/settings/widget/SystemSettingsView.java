package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
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
 *
 * @author Martin Slavkovsky
 */
public class SystemSettingsView extends Composite
        implements SystemSettingsPresenter.SystemSettingsViewInterface, ProvidesValidate {

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
    /** Constants. **/
    private static final int ITEM_HEIGHT = 50;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void setSystemSettings(SettingDetail detail) {
        //notifications
        for (NotificationDetail item : detail.getNotifications()) {
            notifications.add(new NotificationItemView(item));
        }
        fluidContainer.setHeight(((detail.getNotifications().size() + 1) * ITEM_HEIGHT) + "px");
    }

    @Override
    public SettingDetail updateSystemSettings(SettingDetail detail) {
        //notifications
        for (int i = 0; i < detail.getNotifications().size(); i++) {
            NotificationItemView notificationWidget = (NotificationItemView) notifications.getWidget(i);
            NotificationDetail notificationDetail = detail.getNotifications().get(i);

            notificationDetail.setEnabled(notificationWidget.getEnabled());
            notificationDetail.setName(notificationWidget.getName().getText());
            notificationDetail.setPeriod(Period.valueOf(notificationWidget.getPeriod()));
        }

        return detail;
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
