package com.eprovement.poptavka.client.root.header.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.root.interfaces.IMenuView;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class MenuView extends Composite implements IMenuView {

    private static UserMenuViewUiBinder uiBinder = GWT.create(UserMenuViewUiBinder.class);

    interface UserMenuViewUiBinder extends UiBinder<Widget, MenuView> {
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField Button home, client, supplier, demands, createDemand, suppliers, createSupplier, inbox, administration;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public MenuView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public Button getHome() {
        return home;
    }

    @Override
    public Button getClient() {
        return client;
    }

    @Override
    public Button getSupplier() {
        return supplier;
    }

    @Override
    public Button getDemands() {
        return demands;
    }

    @Override
    public Button getCreateDemand() {
        return createDemand;
    }

    @Override
    public Button getSuppliers() {
        return suppliers;
    }

    @Override
    public Button getCreateSupplier() {
        return createSupplier;
    }

    @Override
    public Button getInbox() {
        return inbox;
    }

    @Override
    public Button getAdministration() {
        return administration;
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
    public void menuStyleChange(int loadedModule) {
        switch (loadedModule) {
            case Constants.USER_CLIENT_MODULE:
                clientmenuStyleChange();
                break;
            case Constants.USER_SUPPLIER_MODULE:
                suppliermenuStyleChange();
                break;
            case Constants.USER_MESSAGES_MODULE:
                messagesmenuStyleChange();
                break;
            case Constants.HOME_DEMANDS_MODULE:
                demandsmenuStyleChange();
                break;
            case Constants.CREATE_DEMAND:
                createDemandmenuStyleChange();
                break;
            case Constants.HOME_SUPPLIERS_MODULE:
                suppliersmenuStyleChange();
                break;
            case Constants.CREATE_SUPPLIER:
                createSuppliermenuStyleChange();
                break;
            case Constants.USER_ADMININSTRATION_MODULE:
                administrationmenuStyleChange();
                break;
            default:
                //default style home
                homeMenuStyleChange();
                break;
        }
    }

    /**************************************************************************/
    /* Helper methods.                                                        */
    /**************************************************************************/
    private void homeMenuStyleChange() {
        home.addStyleName(Constants.ACT);
        client.removeStyleName(Constants.ACT);
        supplier.removeStyleName(Constants.ACT);
        demands.removeStyleName(Constants.ACT);
        createDemand.removeStyleName(Constants.ACT);
        suppliers.removeStyleName(Constants.ACT);
        createSupplier.removeStyleName(Constants.ACT);
        inbox.removeStyleName(Constants.ACT);
        administration.removeStyleName(Constants.ACT);
    }

    private void clientmenuStyleChange() {
        home.removeStyleName(Constants.ACT);
        client.addStyleName(Constants.ACT);
        supplier.removeStyleName(Constants.ACT);
        demands.removeStyleName(Constants.ACT);
        createDemand.removeStyleName(Constants.ACT);
        suppliers.removeStyleName(Constants.ACT);
        createSupplier.removeStyleName(Constants.ACT);
        inbox.removeStyleName(Constants.ACT);
        administration.removeStyleName(Constants.ACT);
    }

    private void suppliermenuStyleChange() {
        home.removeStyleName(Constants.ACT);
        client.removeStyleName(Constants.ACT);
        supplier.addStyleName(Constants.ACT);
        demands.removeStyleName(Constants.ACT);
        createDemand.removeStyleName(Constants.ACT);
        suppliers.removeStyleName(Constants.ACT);
        createSupplier.removeStyleName(Constants.ACT);
        inbox.removeStyleName(Constants.ACT);
        administration.removeStyleName(Constants.ACT);
    }

    private void messagesmenuStyleChange() {
        home.removeStyleName(Constants.ACT);
        client.removeStyleName(Constants.ACT);
        supplier.removeStyleName(Constants.ACT);
        demands.removeStyleName(Constants.ACT);
        createDemand.removeStyleName(Constants.ACT);
        suppliers.removeStyleName(Constants.ACT);
        createSupplier.removeStyleName(Constants.ACT);
        inbox.addStyleName(Constants.ACT);
        administration.removeStyleName(Constants.ACT);
    }

    private void demandsmenuStyleChange() {
        home.removeStyleName(Constants.ACT);
        client.removeStyleName(Constants.ACT);
        supplier.removeStyleName(Constants.ACT);
        demands.addStyleName(Constants.ACT);
        createDemand.removeStyleName(Constants.ACT);
        suppliers.removeStyleName(Constants.ACT);
        createSupplier.removeStyleName(Constants.ACT);
        inbox.removeStyleName(Constants.ACT);
        administration.removeStyleName(Constants.ACT);
    }

    private void createDemandmenuStyleChange() {
        home.removeStyleName(Constants.ACT);
        client.removeStyleName(Constants.ACT);
        supplier.removeStyleName(Constants.ACT);
        demands.removeStyleName(Constants.ACT);
        createDemand.addStyleName(Constants.ACT);
        suppliers.removeStyleName(Constants.ACT);
        createSupplier.removeStyleName(Constants.ACT);
        inbox.removeStyleName(Constants.ACT);
        administration.removeStyleName(Constants.ACT);
    }

    private void suppliersmenuStyleChange() {
        home.removeStyleName(Constants.ACT);
        client.removeStyleName(Constants.ACT);
        supplier.removeStyleName(Constants.ACT);
        demands.removeStyleName(Constants.ACT);
        createDemand.removeStyleName(Constants.ACT);
        suppliers.addStyleName(Constants.ACT);
        createSupplier.removeStyleName(Constants.ACT);
        inbox.removeStyleName(Constants.ACT);
        administration.removeStyleName(Constants.ACT);
    }

    private void createSuppliermenuStyleChange() {
        home.removeStyleName(Constants.ACT);
        client.removeStyleName(Constants.ACT);
        supplier.removeStyleName(Constants.ACT);
        demands.removeStyleName(Constants.ACT);
        createDemand.removeStyleName(Constants.ACT);
        suppliers.removeStyleName(Constants.ACT);
        createSupplier.addStyleName(Constants.ACT);
        inbox.removeStyleName(Constants.ACT);
        administration.removeStyleName(Constants.ACT);
    }

    private void administrationmenuStyleChange() {
        home.removeStyleName(Constants.ACT);
        client.removeStyleName(Constants.ACT);
        supplier.removeStyleName(Constants.ACT);
        demands.removeStyleName(Constants.ACT);
        createDemand.removeStyleName(Constants.ACT);
        suppliers.removeStyleName(Constants.ACT);
        createSupplier.removeStyleName(Constants.ACT);
        inbox.removeStyleName(Constants.ACT);
        administration.addStyleName(Constants.ACT);
    }

    @Override
    public void setSupplierButtonVerticalNoLine(boolean noLine) {
        if (noLine) {
            suppliers.setStyleName("button8");
        } else {
            suppliers.setStyleName("button4");
        }
    }
}
