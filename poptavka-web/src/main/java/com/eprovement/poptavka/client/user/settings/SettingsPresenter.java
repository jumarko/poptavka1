package com.eprovement.poptavka.client.user.settings;

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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.Arrays;

@Presenter(view = SettingsView.class)
public class SettingsPresenter
        extends LazyPresenter<SettingsPresenter.HomeSettingsViewInterface, SettingsEventBus> {

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

        StackLayoutPanel getStackPanel();

        SimplePanel getUserSettingsPanel();

        SimplePanel getClientSettingsPanel();

        SimplePanel getSupplierSettingsPanel();

        Button getUpdateButton();

        void showNofity(Boolean updated);

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
        if (!(Storage.getUser() == null && Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL())) {
            eventBus.updateUnreadMessagesCount();
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
                eventBus.loadingShow(Storage.MSGS.updatingProfile());
                updateUserSettings();
                updateClientSettings();
                updateSupplierSettings();
                eventBus.requestUpdateSettings(settingsDetail);
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
        view.showNofity(updated);
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
    private void updateUserSettings() {
        SimplePanel userHolder = (SimplePanel) view.getStackPanel().getWidget(USER_STACK);
        UserSettingsView userSettings = (UserSettingsView) userHolder.getWidget();

        settingsDetail.setCompanyName(userSettings.getCompanyName().getText());
        settingsDetail.setWebsite(userSettings.getWeb().getText());
        settingsDetail.setEmail(userSettings.getEmail().getText());
        settingsDetail.setPhone(userSettings.getPhone().getText());
        settingsDetail.setFirstName(userSettings.getFirstName().getText());
        settingsDetail.setLastName(userSettings.getLastName().getText());
        settingsDetail.setIdentificationNumber(userSettings.getIdentificationNumber().getText());
        settingsDetail.setTaxId(userSettings.getTaxNumber().getText());
        settingsDetail.setDescription(userSettings.getDescriptionBox().getText());
        if (userSettings.getAddress() != null) {
            settingsDetail.setAddresses(Arrays.asList(userSettings.getAddress()));
        }
    }

    /**
     * Update that part of SettingsDetail that belongs to ClientSettings widget.
     */
    private void updateClientSettings() {
        SimplePanel clientHolder = (SimplePanel) view.getStackPanel().getWidget(CLIENT_STACK);
        ClientSettingsView clientSettings = (ClientSettingsView) clientHolder.getWidget();

        //check if clientSettings was event loaded
        if (clientSettings != null) {
            settingsDetail.setClientRating(Integer.parseInt(clientSettings.getClientRating().getText()));
        }
    }

    /**
     * Update that part of SettingsDetail that belongs to SupplierSettings widget.
     */
    private void updateSupplierSettings() {
        SimplePanel supplierHolder = (SimplePanel) view.getStackPanel().getWidget(SUPPLIER_STACK);
        SupplierSettingsView supplierSettings = (SupplierSettingsView) supplierHolder.getWidget();

        //check if supplierSettings was event loaded
        if (supplierSettings != null) {
            if (!supplierSettings.getSupplierRating().getText().isEmpty()) {
                settingsDetail.getSupplier().setOverallRating(Integer.parseInt(
                        supplierSettings.getSupplierRating().getText()));
            }
            settingsDetail.getSupplier().setCategories(supplierSettings.getCategories());
            settingsDetail.getSupplier().setLocalities(supplierSettings.getLocalities());
        }
    }
}
