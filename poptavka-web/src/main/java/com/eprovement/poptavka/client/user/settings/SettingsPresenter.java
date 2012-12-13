package com.eprovement.poptavka.client.user.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.settings.widget.ClientSettingsPresenter;
import com.eprovement.poptavka.client.user.settings.widget.SupplierSettingsPresenter;
import com.eprovement.poptavka.client.user.settings.widget.UserSettingsPresenter;
import com.eprovement.poptavka.shared.domain.settings.SettingsDetail;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

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
    private SettingsDetail settingsDetail;

    //IsWidget musi byt kvoli funkcii ChildAutoDisplay
    public interface HomeSettingsViewInterface extends LazyView, IsWidget {

//        Widget getWidgetView();
//
//        TextBox getCompanyName();
//
//        TextBox getClientRating();
//
//        TextBox getSupplierRating();
//
//        TextBox getStreet();
//
//        TextBox getCity();
//
//        TextBox getZipCode();
//
//        TextArea getCategoriesBox();
//
//        TextArea getLocalitiesBox();
//
//        TextBox getWeb();
//
//        TextBox getEmail();
//
//        TextBox getPhone();
//
//        TextBox getFirstName();
//
//        TextBox getLastName();
//
//        TextBox getIdentificationNumber();
//
//        TextBox getTaxNumber();
//
//        TextArea getDescriptionBox();
//
//        TextArea getServicesBox();
//
//        CheckBox getNewMessageButton();
//
//        ListBox getNewMessageOptions();
//
//        CheckBox getNewDemandButton();
//
//        ListBox getNewDemandOptions();
//
//        CheckBox getNewSystemMessageButton();
//
//        ListBox getNewSystemMessageOptions();
//
//        CheckBox getNewOperatorMessageButton();
//
//        ListBox getNewOperatorMessageOptions();
//
//        CheckBox getDemandStateChangeButton();
//
//        ListBox getDemandStateChangeOptions();
        StackLayoutPanel getStackPanel();

        SimplePanel getUserSettingsPanel();

        SimplePanel getClientSettingsPanel();

        SimplePanel getSupplierSettingsPanel();

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
    public void onSetSettings(SettingsDetail detail) {

        //Just store??
        this.settingsDetail = detail;
        //volat cez eventBus alebo cez prezenter?
//        userPresenter.onSetUserSettings(detail);
        eventBus.setUserSettings(detail);
//        GWT.log("SettingsDetail company name" + detail.getCompanyName());
//        view.getCompanyName().setText(detail.getCompanyName());
//        view.getClientRating().setText(
//                Integer.toString(detail.getClientRating()));
//        if (detail.getSupplier() != null
//                && detail.getSupplier().getOverallRating() != null) {
//            view.getSupplierRating().setText(
//                    Integer.toString(detail.getSupplier().getOverallRating()));
//        }
//        view.getStreet().setText(detail.getAddresses().get(0).getStreet());
//        view.getCity().setText(detail.getAddresses().get(0).getCity());
//        view.getZipCode().setText(detail.getAddresses().get(0).getZipCode());
//
//        StringBuilder categoryBuilder = new StringBuilder();
//        if (detail.getSupplier() != null) {
//            for (CategoryDetail category : detail.getSupplier().getCategories()) {
//                categoryBuilder.append(category.getName());
//                categoryBuilder.append("\n");
//            }
//        }
//        view.getCategoriesBox().setText(categoryBuilder.toString());
//
//        StringBuilder localitiesBuilder = new StringBuilder();
//        if (detail.getSupplier() != null) {
//            for (LocalityDetail locality : detail.getSupplier().getLocalities()) {
//                localitiesBuilder.append(locality.getName());
//                localitiesBuilder.append("\n");
//            }
//        }
//        view.getLocalitiesBox().setText(localitiesBuilder.toString());
//        view.getEmail().setText(detail.getEmail());
//        view.getPhone().setText(detail.getPhone());
//        view.getFirstName().setText(detail.getFirstName());
//        view.getLastName().setText(detail.getLastName());
//        view.getIdentificationNumber().setText(detail.getIdentificationNumber());
//        view.getTaxNumber().setText(detail.getTaxId());
//        view.getDescriptionBox().setText(detail.getDescription());
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
}
