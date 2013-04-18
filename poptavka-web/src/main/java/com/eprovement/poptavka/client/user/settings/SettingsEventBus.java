package com.eprovement.poptavka.client.user.settings;

import com.eprovement.poptavka.client.root.BaseChildEventBus;
import com.eprovement.poptavka.client.user.settings.widget.ClientSettingsPresenter;
import com.eprovement.poptavka.client.user.settings.widget.SupplierSettingsPresenter;
import com.eprovement.poptavka.client.user.settings.widget.SystemSettingsPresenter;
import com.eprovement.poptavka.client.user.settings.widget.UserSettingsPresenter;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Debug.LogLevel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;

import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.event.EventBusWithLookup;
import java.util.List;

@Debug(logLevel = LogLevel.DETAILED)
@Events(startPresenter = SettingsPresenter.class, module = SettingsModule.class)
public interface SettingsEventBus extends EventBusWithLookup, BaseChildEventBus {

    /**
     * Start event is called only when module is instantiated first time.
     * We can use it for history initialization.
     */
    @Start
    @Event(handlers = SettingsPresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. It there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     * We can use forward event to switch css style for selected menu button.
     */
    @Forward
    @Event(handlers = SettingsPresenter.class)
    void forward();

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**
     * The only entry point to this module due to code-spliting feature.
     */
    @Event(handlers = SettingsPresenter.class, historyConverter = SettingsHistoryConverter.class,
            navigationEvent = true)
    String goToSettingsModule();

    /**************************************************************************/
    /* Parent events                                                          */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void initAddressWidget(SimplePanel embedToWidget);

    @Event(forwardToParent = true)
    void initCategoryWidget(SimplePanel embedToWidget, int checkboxes, int displayCountsOfWhat,
        List<CategoryDetail> categoriesToSet, boolean selectionRestriction);

    @Event(forwardToParent = true)
    void initLocalityWidget(SimplePanel embedToWidget, int checkboxes, int displayCountsOfWhat,
        List<LocalityDetail> localitiesToSet, boolean selectionRestriction);

    @Event(forwardToParent = true)
    void initServicesWidget(SimplePanel embedToWidget);

    @Event(forwardToParent = true)
    void setUpdatedUnreadMessagesCount(UnreadMessagesDetail numberOfMessages);

    /**************************************************************************/
    /* Business events handled by Presenters.                                 */
    /**************************************************************************/
    @Event(handlers = UserSettingsPresenter.class)
    void notifyAddressWidgetListeners();

    @Event(handlers = SupplierSettingsPresenter.class)
    void nofityServicesWidgetListeners();

    @Event(handlers = SettingsPresenter.class)
    void setSettings(SettingDetail detail);

    @Event(handlers = SettingsPresenter.class)
    void responseUpdateSettings(Boolean updated);

    @Event(handlers = SettingsPresenter.class)
    void updateUserStatus(boolean isChange);

    @Event(handlers = SettingsPresenter.class)
    void updateClientStatus(boolean isChange);

    @Event(handlers = SettingsPresenter.class)
    void updateSupplierStatus(boolean isChange);

    @Event(handlers = SettingsPresenter.class)
    void updateSystemStatus(boolean isChange);

    /**************************************************************************/
    /* Business events handled by UserSettingsPresenter.                      */
    /**************************************************************************/
    @Event(handlers = UserSettingsPresenter.class)
    void setUserSettings(SettingDetail detail);

    @Event(handlers = ClientSettingsPresenter.class)
    void setClientSettings(SettingDetail detail);

    @Event(handlers = SupplierSettingsPresenter.class)
    void setSupplierSettings(SettingDetail detail);

    @Event(handlers = SystemSettingsPresenter.class)
    void setSystemSettings(SettingDetail detail);

    /**************************************************************************/
    /* Business events handled by Handlers.                                   */
    /**************************************************************************/
    @Event(handlers = SettingsHandler.class)
    void getLoggedUser(long userId);

    @Event(handlers = SettingsHandler.class)
    void updateUnreadMessagesCount();

    @Event(handlers = SettingsHandler.class)
    void requestUpdateSettings(SettingDetail settingsDetail);
}
