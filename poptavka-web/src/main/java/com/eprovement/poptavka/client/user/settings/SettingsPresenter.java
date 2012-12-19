package com.eprovement.poptavka.client.user.settings;

import com.eprovement.poptavka.client.common.StatusIconLabel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.settings.widget.ClientSettingsPresenter;
import com.eprovement.poptavka.client.user.settings.widget.ClientSettingsView;
import com.eprovement.poptavka.client.user.settings.widget.SupplierSettingsPresenter;
import com.eprovement.poptavka.client.user.settings.widget.SupplierSettingsView;
import com.eprovement.poptavka.client.user.settings.widget.UserSettingsPresenter;
import com.eprovement.poptavka.client.user.settings.widget.UserSettingsView;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.mvp4g.client.history.NavigationConfirmationInterface;
import com.mvp4g.client.history.NavigationEventCommand;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

@Presenter(view = SettingsView.class)
public class SettingsPresenter
        extends LazyPresenter<SettingsPresenter.HomeSettingsViewInterface, SettingsEventBus>
        implements NavigationConfirmationInterface {

    private static final int USER_STACK = 0;
    private static final int CLIENT_STACK = 1;
    private static final int SUPPLIER_STACK = 2;
    //
    private UserSettingsPresenter userPresenter = null;
    private ClientSettingsPresenter clientPresenter = null;
    private SupplierSettingsPresenter supplierPresenter = null;
    //
    private SettingDetail settingsDetail;

    //IsWidget musi byt kvoli funkcii ChildAutoDisplay
    public interface HomeSettingsViewInterface extends LazyView, IsWidget {

        /** Setters. **/
        void updateUserStatus(boolean isChange);

        void updateClientStatus(boolean isChange);

        void updateSupplierStatus(boolean isChange);

        /** Getters. **/
        StackLayoutPanel getStackPanel();

        SimplePanel getUserSettingsPanel();

        SimplePanel getClientSettingsPanel();

        SimplePanel getSupplierSettingsPanel();

        StatusIconLabel getUserInfoStatus();

        Button getUpdateButton();

        void showNofity(String message, Boolean updated);

        Widget getWidgetView();
    }

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing
    }

    /**
     * Every call of onForward method invokes updateUnreadMessagesCount event that is secured thus user without
     * particular access role can't access it and loginPopupView will be displayed.
     */
    public void onForward() {
//        eventBus.setNavigationConfirmation(this);
        if (!(Storage.getUser() == null && Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL())) {
            eventBus.updateUnreadMessagesCount();
        }
    }

    @Override
    public void confirm(NavigationEventCommand event) {
        //pseudo method to verify if the view has changed
        boolean isUserChange = ((UserSettingsView) view.getUserSettingsPanel().getWidget()).isSettingChange();
        boolean isClientChange = false;
        boolean isSupplierChange = false;
        if (view.getClientSettingsPanel().getWidget() != null) { //if not yet even initialized
            isClientChange = ((ClientSettingsView) view.getClientSettingsPanel().getWidget()).isSettingChange();
        }
        if (view.getSupplierSettingsPanel().getWidget() != null) { //if not yet even initialized
            isSupplierChange = ((SupplierSettingsView) view.getSupplierSettingsPanel().getWidget()).isSettingChange();
        }
        if (isUserChange && isClientChange && isSupplierChange) {
            //Window shouldn't be used inside a presenter
            //this is just to give a simple example
            if (Window.confirm(Storage.MSGS.notificationLeavingPage())) {
                updateProfile();
                event.fireEvent();
            }
        }
    }

    /**************************************************************************/
    /* Bind                                                                   */
    /**************************************************************************/
    @Override
    public void bindView() {
        view.getStackPanel().addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
            @Override
            public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
                switch (event.getItem()) {
                    case USER_STACK:
                        if (view.getUserSettingsPanel().getWidget() == null) {
                            initUserSettings(view.getUserSettingsPanel());
                        }
                        break;
                    case CLIENT_STACK:
                        if (view.getClientSettingsPanel().getWidget() == null) {
                            initClientSettings(view.getClientSettingsPanel());
                            eventBus.setClientSettings(settingsDetail);
                        }
                        break;
                    case SUPPLIER_STACK:
                        if (view.getSupplierSettingsPanel().getWidget() == null) {
                            initSupplierSettings(view.getSupplierSettingsPanel());
                            eventBus.setSupplierSettings(settingsDetail);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        view.getUpdateButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                updateProfile();
            }
        });
    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    public void onGoToSettingsModule() {
        eventBus.loadingShow(Storage.MSGS.loading());
        initUserSettings(view.getUserSettingsPanel());
        GWT.log("User ID for settings" + Storage.getUser().getUserId());

        eventBus.getLoggedUser(Storage.getUser().getUserId());

    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/
    public void onSetSettings(SettingDetail detail) {
        this.settingsDetail = detail;
        //set userSettings widget because it is loaded on startup
        eventBus.setUserSettings(detail);
    }

    public void onResponseUpdateSettings(Boolean updated) {
        eventBus.loadingHide();
        if (updated) {
            view.showNofity(Storage.MSGS.updatedOK(), updated);
        } else {
            view.showNofity(Storage.MSGS.updatedNotOK(), updated);
        }
    }

    public void onUpdateUserStatus(boolean isChange) {
        view.updateUserStatus(isChange);
    }

    public void onUpdateClientStatus(boolean isChange) {
        view.updateClientStatus(isChange);
    }

    public void onUpdateSupplierStatus(boolean isChange) {
        view.updateSupplierStatus(isChange);
    }

    /**************************************************************************/
    /* Init Methods                                                           */
    /**************************************************************************/
    public void initUserSettings(SimplePanel holder) {
        if (userPresenter != null) {
            eventBus.removeHandler(userPresenter);
        }
        userPresenter = eventBus.addHandler(UserSettingsPresenter.class);
        userPresenter.initUserSettings(holder);
    }

    public void initClientSettings(SimplePanel holder) {
        if (clientPresenter != null) {
            eventBus.removeHandler(clientPresenter);
        }
        clientPresenter = eventBus.addHandler(ClientSettingsPresenter.class);
        clientPresenter.initUserSettings(holder);
    }

    public void initSupplierSettings(SimplePanel holder) {
        if (supplierPresenter != null) {
            eventBus.removeHandler(supplierPresenter);
        }
        supplierPresenter = eventBus.addHandler(SupplierSettingsPresenter.class);
        supplierPresenter.initUserSettings(holder);
    }

    /**************************************************************************/
    /* Update settings methods                                                */
    /**************************************************************************/
    /**
     * Update that part of SettingsDetail that belongs to UserSettings widget.
     */
    private boolean updateUserSettings() {
        SimplePanel userHolder = (SimplePanel) view.getStackPanel().getWidget(USER_STACK);
        UserSettingsView userSettings = (UserSettingsView) userHolder.getWidget();

        if (userSettings != null && userSettings.isSettingChange()) {
            settingsDetail = userSettings.updateUserSettings(settingsDetail);
            return true;
        }
        return false;
    }

    /**
     * Update that part of SettingsDetail that belongs to ClientSettings widget.
     */
    private boolean updateClientSettings() {
        SimplePanel clientHolder = (SimplePanel) view.getStackPanel().getWidget(CLIENT_STACK);
        ClientSettingsView clientSettings = (ClientSettingsView) clientHolder.getWidget();

        //check if clientSettings was event loaded
        if (clientSettings != null && clientSettings.isSettingChange()) {
            settingsDetail = clientSettings.updateClientSettings(settingsDetail);
            return true;
        }
        return false;
    }

    /**
     * Update that part of SettingsDetail that belongs to SupplierSettings widget.
     */
    private boolean updateSupplierSettings() {
        SimplePanel supplierHolder = (SimplePanel) view.getStackPanel().getWidget(SUPPLIER_STACK);
        SupplierSettingsView supplierSettings = (SupplierSettingsView) supplierHolder.getWidget();

        //check if supplierSettings was event loaded
        if (supplierSettings != null && supplierSettings.isSettingChange()) {
            settingsDetail = supplierSettings.updateSupplierSettings(settingsDetail);
            return true;
        }
        return false;
    }

    private void updateProfile() {
        eventBus.loadingShow(Storage.MSGS.updatingProfile());
        boolean update = updateUserSettings();
        update = update || updateClientSettings();
        update = update || updateSupplierSettings();
        if (update) {
            eventBus.requestUpdateSettings(settingsDetail);
        } else {
            eventBus.loadingHide();
            view.showNofity(Storage.MSGS.nothingToUpdate(), true);
        }
    }
}
