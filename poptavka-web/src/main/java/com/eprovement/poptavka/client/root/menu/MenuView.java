package com.eprovement.poptavka.client.root.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.client.root.ReverseCompositeView;
import com.eprovement.poptavka.client.root.interfaces.IMenuView;
import com.eprovement.poptavka.client.root.interfaces.IMenuView.IMenuPresenter;
import com.google.gwt.dom.client.UListElement;

public class MenuView extends ReverseCompositeView<IMenuPresenter> implements IMenuView {

    private static MenuViewUiBinder uiBinder = GWT.create(MenuViewUiBinder.class);

    interface MenuViewUiBinder extends UiBinder<Widget, MenuView> {
    }
    @UiField
    UListElement menuList;
    @UiField
    Button home, demands, suppliers, createSupplier, createDemand;

    public MenuView() {
        initWidget(uiBinder.createAndBindUi(this));
        home.addStyleName(StyleResource.INSTANCE.layout().selected());
    }

    /**************************************************************************/
    /* UiHanders.                                                             */
    /**************************************************************************/
    @UiHandler("home")
    public void onClickHome(ClickEvent e) {
        presenter.goToHomeWelcome();
        homeMenuStyleChange();
    }

    @UiHandler("demands")
    public void onClickDemands(ClickEvent e) {
        presenter.goToHomeDemands();
        demandsMenuStyleChange();
    }

    @UiHandler("suppliers")
    public void onClickSuppliers(ClickEvent e) {
        presenter.goToHomeSuppliers();
        suppliersMenuStyleChange();
    }

    @UiHandler("createSupplier")
    public void onClickCreateSupplier(ClickEvent e) {
        presenter.goToCreateSupplier();
        createSupplierMenuStyleChange();
    }

    @UiHandler("createDemand")
    public void onClickCreateDemand(ClickEvent e) {
        presenter.goToCreateDemand();
        createDemandMenuStyleChange();
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
                homeMenuStyleChange();
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
    private void homeMenuStyleChange() {
        home.addStyleName(StyleResource.INSTANCE.layout().selected());
        demands.removeStyleName(StyleResource.INSTANCE.layout().selected());
        suppliers.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createSupplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createDemand.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    private void demandsMenuStyleChange() {
        home.removeStyleName(StyleResource.INSTANCE.layout().selected());
        demands.addStyleName(StyleResource.INSTANCE.layout().selected());
        suppliers.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createSupplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createDemand.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    private void suppliersMenuStyleChange() {
        home.removeStyleName(StyleResource.INSTANCE.layout().selected());
        demands.removeStyleName(StyleResource.INSTANCE.layout().selected());
        suppliers.addStyleName(StyleResource.INSTANCE.layout().selected());
        createSupplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createDemand.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    private void createSupplierMenuStyleChange() {
        home.removeStyleName(StyleResource.INSTANCE.layout().selected());
        demands.removeStyleName(StyleResource.INSTANCE.layout().selected());
        suppliers.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createSupplier.addStyleName(StyleResource.INSTANCE.layout().selected());
        createDemand.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    private void createDemandMenuStyleChange() {
        home.removeStyleName(StyleResource.INSTANCE.layout().selected());
        demands.removeStyleName(StyleResource.INSTANCE.layout().selected());
        suppliers.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createSupplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createDemand.addStyleName(StyleResource.INSTANCE.layout().selected());
    }
}
