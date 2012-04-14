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
        clickHomeMenuStyleChange();
    }

    @UiHandler("demands")
    public void clickDemands(ClickEvent e) {
        presenter.goToHomeDemands();
        clickDemandsMenuStyleChange();
    }

    @UiHandler("suppliers")
    public void clickSuppliers(ClickEvent e) {
        presenter.goToHomeSuppliers();
        clickSuppliersMenuStyleChange();
    }

    @UiHandler("createSupplier")
    public void clickCreateSupplier(ClickEvent e) {
        presenter.goToCreateSupplier();
        clickCreateSupplierMenuStyleChange();
    }

    @UiHandler("createDemand")
    public void clickCreateDemand(ClickEvent e) {
        presenter.goToCreateDemand();
        clickCreateDemandMenuStyleChange();
    }

    /**************************************************************************/
    /* Style change methods.                                                  */
    /**************************************************************************/
    @Override
    public void clickHomeMenuStyleChange() {
        home.addStyleName(StyleResource.INSTANCE.layout().selected());
        demands.removeStyleName(StyleResource.INSTANCE.layout().selected());
        suppliers.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createSupplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createDemand.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    @Override
    public void clickDemandsMenuStyleChange() {
        home.removeStyleName(StyleResource.INSTANCE.layout().selected());
        demands.addStyleName(StyleResource.INSTANCE.layout().selected());
        suppliers.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createSupplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createDemand.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    @Override
    public void clickSuppliersMenuStyleChange() {
        home.removeStyleName(StyleResource.INSTANCE.layout().selected());
        demands.removeStyleName(StyleResource.INSTANCE.layout().selected());
        suppliers.addStyleName(StyleResource.INSTANCE.layout().selected());
        createSupplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createDemand.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    @Override
    public void clickCreateSupplierMenuStyleChange() {
        home.removeStyleName(StyleResource.INSTANCE.layout().selected());
        demands.removeStyleName(StyleResource.INSTANCE.layout().selected());
        suppliers.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createSupplier.addStyleName(StyleResource.INSTANCE.layout().selected());
        createDemand.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    @Override
    public void clickCreateDemandMenuStyleChange() {
        home.removeStyleName(StyleResource.INSTANCE.layout().selected());
        demands.removeStyleName(StyleResource.INSTANCE.layout().selected());
        suppliers.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createSupplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createDemand.addStyleName(StyleResource.INSTANCE.layout().selected());
    }
}
