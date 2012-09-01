package com.eprovement.poptavka.client.root.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.client.root.ReverseCompositeView;
import com.eprovement.poptavka.client.root.interfaces.IUserMenuView;
import com.eprovement.poptavka.client.root.interfaces.IUserMenuView.IUserMenuPresenter;
import com.google.gwt.dom.client.UListElement;

public class UserMenuView extends ReverseCompositeView<IUserMenuPresenter> implements IUserMenuView {

    private static UserMenuViewUiBinder uiBinder = GWT.create(UserMenuViewUiBinder.class);

    interface UserMenuViewUiBinder extends UiBinder<Widget, UserMenuView> {
    }
    @UiField
    UListElement menuList;
    @UiField
    Button client, supplier, messages, settings, administration;

    public UserMenuView() {
        initWidget(uiBinder.createAndBindUi(this));
        menuList.addClassName(StyleResource.INSTANCE.layout().homeMenu());
        client.addStyleName(StyleResource.INSTANCE.layout().selected());
    }

    /**************************************************************************/
    /* UiHanders.                                                             */
    /**************************************************************************/
    @UiHandler("client")
    public void onClickClient(ClickEvent e) {
        presenter.goToClient();
        clientUserMenuStyleChange();
    }

    @UiHandler("supplier")
    public void onClickSupplier(ClickEvent e) {
        presenter.goToSupplier();
        supplierUserMenuStyleChange();
    }

    @UiHandler("messages")
    public void onClickMessages(ClickEvent e) {
        presenter.goToMessages();
        messagesUserMenuStyleChange();
    }

    @UiHandler("settings")
    public void onClickSettings(ClickEvent e) {
        presenter.goToSettings();
        settingsUserMenuStyleChange();
    }

    @UiHandler("administration")
    public void onClickAdministration(ClickEvent e) {
        presenter.goToAdministration();
        administrationUserMenuStyleChange();
    }

    /**************************************************************************/
    /* Style change methods.                                                  */
    /**************************************************************************/
    /**
     * Loads right styles to menu buttons.
     *
     * @param loadedModule - use module constants from class Contants.
     */
    @Override
    public void userMenuStyleChange(int loadedModule) {
        switch (loadedModule) {
            case Constants.USER_CLIENT_MODULE:
                clientUserMenuStyleChange();
                break;
            case Constants.USER_SUPPLIER_MODULE:
                supplierUserMenuStyleChange();
                break;
            case Constants.USER_MESSAGES_MODULE:
                messagesUserMenuStyleChange();
                break;
            case Constants.USER_SETTINGS_MODULE:
                settingsUserMenuStyleChange();
                break;
            case Constants.USER_ADMININSTRATION_MODULE:
                administrationUserMenuStyleChange();
                break;
            default:
                break;
        }
    }

    /**************************************************************************/
    /* Helper methods.                                                        */
    /**************************************************************************/
    private void clientUserMenuStyleChange() {
        client.addStyleName(StyleResource.INSTANCE.layout().selected());
        supplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        messages.removeStyleName(StyleResource.INSTANCE.layout().selected());
        settings.removeStyleName(StyleResource.INSTANCE.layout().selected());
        administration.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    private void supplierUserMenuStyleChange() {
        client.removeStyleName(StyleResource.INSTANCE.layout().selected());
        supplier.addStyleName(StyleResource.INSTANCE.layout().selected());
        messages.removeStyleName(StyleResource.INSTANCE.layout().selected());
        settings.removeStyleName(StyleResource.INSTANCE.layout().selected());
        administration.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    private void messagesUserMenuStyleChange() {
        client.removeStyleName(StyleResource.INSTANCE.layout().selected());
        supplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        messages.addStyleName(StyleResource.INSTANCE.layout().selected());
        settings.removeStyleName(StyleResource.INSTANCE.layout().selected());
        administration.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    private void settingsUserMenuStyleChange() {
        client.removeStyleName(StyleResource.INSTANCE.layout().selected());
        supplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        messages.removeStyleName(StyleResource.INSTANCE.layout().selected());
        settings.addStyleName(StyleResource.INSTANCE.layout().selected());
        administration.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    private void administrationUserMenuStyleChange() {
        client.removeStyleName(StyleResource.INSTANCE.layout().selected());
        supplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        messages.removeStyleName(StyleResource.INSTANCE.layout().selected());
        settings.removeStyleName(StyleResource.INSTANCE.layout().selected());
        administration.addStyleName(StyleResource.INSTANCE.layout().selected());
    }

    @Override
    public void setTabVisibility(int module, boolean visible) {
        switch (module) {
            case Constants.USER_CLIENT_MODULE:
                client.setVisible(visible);
                break;
            case Constants.USER_SUPPLIER_MODULE:
                supplier.setVisible(visible);
                break;
            case Constants.USER_ADMININSTRATION_MODULE:
                administration.setVisible(visible);
                break;
            default:
                break;
        }
    }
}
