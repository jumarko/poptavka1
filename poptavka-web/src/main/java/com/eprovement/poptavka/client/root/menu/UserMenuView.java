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
    Button client, supplier, demands, createDemand, suppliers, createSupplier, inbox, administration;

    public UserMenuView() {
        initWidget(uiBinder.createAndBindUi(this));
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

    @UiHandler("inbox")
    public void onClickMessages(ClickEvent e) {
        presenter.goToMessages();
        messagesUserMenuStyleChange();
    }

    @UiHandler("demands")
    public void onClickDemands(ClickEvent e) {
        presenter.goToDemands();
    }

    @UiHandler("createDemand")
    public void onClickCreateDemand(ClickEvent e) {
        presenter.goToCreateDemands();
    }

    @UiHandler("suppliers")
    public void onClickSuppliers(ClickEvent e) {
        presenter.goToSuppliers();
    }

    @UiHandler("createSupplier")
    public void onClickCreateSupplier(ClickEvent e) {
        presenter.goToCreateSupplier();
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
            case Constants.HOME_DEMANDS_MODULE:
                demandsUserMenuStyleChange();
                break;
            case Constants.CREATE_DEMAND:
                createDemandUserMenuStyleChange();
                break;
            case Constants.HOME_SUPPLIERS_MODULE:
                suppliersUserMenuStyleChange();
                break;
            case Constants.CREATE_SUPPLIER:
                createSupplierUserMenuStyleChange();
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
        demands.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createDemand.removeStyleName(StyleResource.INSTANCE.layout().selected());
        suppliers.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createSupplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        inbox.removeStyleName(StyleResource.INSTANCE.layout().selected());
        administration.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    private void supplierUserMenuStyleChange() {
        client.removeStyleName(StyleResource.INSTANCE.layout().selected());
        supplier.addStyleName(StyleResource.INSTANCE.layout().selected());
        demands.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createDemand.removeStyleName(StyleResource.INSTANCE.layout().selected());
        suppliers.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createSupplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        inbox.removeStyleName(StyleResource.INSTANCE.layout().selected());
        administration.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    private void messagesUserMenuStyleChange() {
        client.removeStyleName(StyleResource.INSTANCE.layout().selected());
        supplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        demands.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createDemand.removeStyleName(StyleResource.INSTANCE.layout().selected());
        suppliers.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createSupplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        inbox.addStyleName(StyleResource.INSTANCE.layout().selected());
        administration.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    private void demandsUserMenuStyleChange() {
        client.removeStyleName(StyleResource.INSTANCE.layout().selected());
        supplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        demands.addStyleName(StyleResource.INSTANCE.layout().selected());
        createDemand.removeStyleName(StyleResource.INSTANCE.layout().selected());
        suppliers.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createSupplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        inbox.removeStyleName(StyleResource.INSTANCE.layout().selected());
        administration.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    private void createDemandUserMenuStyleChange() {
        client.removeStyleName(StyleResource.INSTANCE.layout().selected());
        supplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        demands.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createDemand.addStyleName(StyleResource.INSTANCE.layout().selected());
        suppliers.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createSupplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        inbox.removeStyleName(StyleResource.INSTANCE.layout().selected());
        administration.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    private void suppliersUserMenuStyleChange() {
        client.removeStyleName(StyleResource.INSTANCE.layout().selected());
        supplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        demands.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createDemand.removeStyleName(StyleResource.INSTANCE.layout().selected());
        suppliers.addStyleName(StyleResource.INSTANCE.layout().selected());
        createSupplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        inbox.removeStyleName(StyleResource.INSTANCE.layout().selected());
        administration.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    private void createSupplierUserMenuStyleChange() {
        client.removeStyleName(StyleResource.INSTANCE.layout().selected());
        supplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        demands.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createDemand.removeStyleName(StyleResource.INSTANCE.layout().selected());
        suppliers.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createSupplier.addStyleName(StyleResource.INSTANCE.layout().selected());
        inbox.removeStyleName(StyleResource.INSTANCE.layout().selected());
        administration.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    private void administrationUserMenuStyleChange() {
        client.removeStyleName(StyleResource.INSTANCE.layout().selected());
        supplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        demands.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createDemand.removeStyleName(StyleResource.INSTANCE.layout().selected());
        suppliers.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createSupplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        inbox.removeStyleName(StyleResource.INSTANCE.layout().selected());
        administration.addStyleName(StyleResource.INSTANCE.layout().selected());
    }

    /**
     * Sets tab visibility. According to given attribute module appropriate tab
     * sets its visibility according to given attribute visible.
     * If Client or Supplier tab is going to be displayed (visible attr = true),
     * its also sets selected style on appropriate menu button.
     *
     * @param module
     * @param visible
     */
    @Override
    public void setTabVisibility(int module, boolean visible) {
        switch (module) {
            case Constants.USER_CLIENT_MODULE:
                client.setVisible(visible);
                break;
            case Constants.USER_SUPPLIER_MODULE:
                supplier.setVisible(visible);
                break;
            case Constants.USER_MESSAGES_MODULE:
                inbox.setVisible(visible);
                break;
            case Constants.HOME_DEMANDS_MODULE:
                demands.setVisible(visible);
                break;
            case Constants.CREATE_DEMAND:
                createDemand.setVisible(visible);
                break;
            case Constants.HOME_SUPPLIERS_MODULE:
                suppliers.setVisible(visible);
                break;
            case Constants.CREATE_SUPPLIER:
                createSupplier.setVisible(visible);
                break;
            case Constants.USER_ADMININSTRATION_MODULE:
                administration.setVisible(visible);
                break;
            default:
                break;
        }
    }
}
