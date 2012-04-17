package cz.poptavka.sample.client.root.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.client.root.ReverseCompositeView;
import cz.poptavka.sample.client.root.interfaces.IMenuView;
import cz.poptavka.sample.client.root.interfaces.IMenuView.IMenuPresenter;

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
    public void clickDemands(ClickEvent e) {
        presenter.goToHomeDemands();
        demandsMenuStyleChange();
    }

    @UiHandler("suppliers")
    public void clickSuppliers(ClickEvent e) {
        presenter.goToHomeSuppliers();
        suppliersMenuStyleChange();
    }

    @UiHandler("createSupplier")
    public void clickCreateSupplier(ClickEvent e) {
        presenter.goToCreateSupplier();
        createSupplierMenuStyleChange();
    }

    @UiHandler("createDemand")
    public void clickCreateDemand(ClickEvent e) {
        presenter.goToCreateDemand();
        createDemandMenuStyleChange();
    }

    /**************************************************************************/
    /* Style change methods.                                                  */
    /**************************************************************************/
    /**
     * Loads right styles to menu buttons.
     * @param loadedModule - 0 - HomeWelcome
     *                     - 1 - HomeDemands
     *                     - 2 - HomeSuppliers
     *                     - 3 - DemandCreation
     *                     - 4 - SupplierCreation
     */
    @Override
    public void menuStyleChange(int loadedModule) {
        switch (loadedModule) {
            case 0:
                homeMenuStyleChange();
                break;
            case 1:
                demandsMenuStyleChange();
                break;
            case 2:
                suppliersMenuStyleChange();
                break;
            case 3:
                createDemandMenuStyleChange();
                break;
            case 4:
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
