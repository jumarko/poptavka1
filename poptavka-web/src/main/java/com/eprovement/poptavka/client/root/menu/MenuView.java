package com.eprovement.poptavka.client.root.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.root.ReverseCompositeView;
import com.eprovement.poptavka.client.root.interfaces.IMenuView;
import com.eprovement.poptavka.client.root.interfaces.IMenuView.IMenuPresenter;
import com.google.gwt.dom.client.UListElement;

public class MenuView extends ReverseCompositeView<IMenuPresenter> implements IMenuView {

    private static MenuViewUiBinder uiBinder = GWT.create(MenuViewUiBinder.class);

    interface MenuViewUiBinder extends UiBinder<Widget, MenuView> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField UListElement menuList;
    @UiField Button home, demands, suppliers, createSupplier, createDemand;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public MenuView() {
        initWidget(uiBinder.createAndBindUi(this));
        home.addStyleName(Constants.ACT);
    }

    /**************************************************************************/
    /* UiHanders.                                                             */
    /**************************************************************************/
    @UiHandler("home")
    public void onClickHome(ClickEvent e) {
        presenter.goToHomeWelcome();
    }

    @UiHandler("demands")
    public void onClickDemands(ClickEvent e) {
        presenter.goToHomeDemands();
    }

    @UiHandler("suppliers")
    public void onClickSuppliers(ClickEvent e) {
        presenter.goToHomeSuppliers();
    }

    @UiHandler("createSupplier")
    public void onClickCreateSupplier(ClickEvent e) {
        presenter.goToCreateSupplier();
    }

    @UiHandler("createDemand")
    public void onClickCreateDemand(ClickEvent e) {
        presenter.goToCreateDemand();
    }

    /**************************************************************************/
    /* Style change methods.                                                  */
    /**************************************************************************/
    /**
     * Loads right styles to menu buttons.
     * @param loadedModule - use module constants from class Contants.
     */
    @Override
    public void menuStyleChange(int loadedModule) {
        switch (loadedModule) {
            case Constants.HOME_WELCOME_MODULE:
                menuStyleChange();
                break;
            case Constants.HOME_DEMANDS_MODULE:
                demandsMenuStyleChange();
                break;
            case Constants.HOME_SUPPLIERS_MODULE:
                suppliersMenuStyleChange();
                break;
            case Constants.HOME_DEMAND_CREATION_MODULE:
                createDemandMenuStyleChange();
                break;
            case Constants.HOME_SUPPLIER_CREATION_MODULE:
                createSupplierMenuStyleChange();
                break;
            default:
                break;
        }
    }

    /**************************************************************************/
    /* Helper methods.                                                        */
    /**************************************************************************/
    private void menuStyleChange() {
        home.addStyleName(Constants.ACT);
        demands.removeStyleName(Constants.ACT);
        suppliers.removeStyleName(Constants.ACT);
        createSupplier.removeStyleName(Constants.ACT);
        createDemand.removeStyleName(Constants.ACT);
    }

    private void demandsMenuStyleChange() {
        home.removeStyleName(Constants.ACT);
        demands.addStyleName(Constants.ACT);
        suppliers.removeStyleName(Constants.ACT);
        createSupplier.removeStyleName(Constants.ACT);
        createDemand.removeStyleName(Constants.ACT);
    }

    private void suppliersMenuStyleChange() {
        home.removeStyleName(Constants.ACT);
        demands.removeStyleName(Constants.ACT);
        suppliers.addStyleName(Constants.ACT);
        createSupplier.removeStyleName(Constants.ACT);
        createDemand.removeStyleName(Constants.ACT);
    }

    private void createSupplierMenuStyleChange() {
        home.removeStyleName(Constants.ACT);
        demands.removeStyleName(Constants.ACT);
        suppliers.removeStyleName(Constants.ACT);
        createSupplier.addStyleName(Constants.ACT);
        createDemand.removeStyleName(Constants.ACT);
    }

    private void createDemandMenuStyleChange() {
        home.removeStyleName(Constants.ACT);
        demands.removeStyleName(Constants.ACT);
        suppliers.removeStyleName(Constants.ACT);
        createSupplier.removeStyleName(Constants.ACT);
        createDemand.addStyleName(Constants.ACT);
    }
}
