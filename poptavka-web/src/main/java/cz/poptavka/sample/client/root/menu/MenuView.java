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
    Button home, demands, suppliers, createSupplier, createDemand, page1, page2;

    public MenuView() {
        initWidget(uiBinder.createAndBindUi(this));
        // TODO Praso - zistit, ktory z tychto 2 konstruktorov MainView sa pouziva.
        // Jeden z nich mozeme asi zmazat
    }

    public MenuView(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
        menuList.addClassName(StyleResource.INSTANCE.layout().homeMenu());
    }

    @UiHandler("home")
    public void onClickHome(ClickEvent e) {
        presenter.goToHomeWelcome();
        home.setStyleName(StyleResource.INSTANCE.layout().selected());
        demands.removeStyleName(StyleResource.INSTANCE.layout().selected());
        suppliers.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createSupplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createDemand.removeStyleName(StyleResource.INSTANCE.layout().selected());
        page1.removeStyleName(StyleResource.INSTANCE.layout().selected());
        page2.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    @UiHandler("demands")
    public void onClickDemands(ClickEvent e) {
        presenter.goToHomeDemands();
        home.removeStyleName(StyleResource.INSTANCE.layout().selected());
        demands.setStyleName(StyleResource.INSTANCE.layout().selected());
        suppliers.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createSupplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createDemand.removeStyleName(StyleResource.INSTANCE.layout().selected());
        page1.removeStyleName(StyleResource.INSTANCE.layout().selected());
        page2.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    @UiHandler("suppliers")
    public void onClickSuppliers(ClickEvent e) {
        presenter.goToHomeSuppliers();
        home.removeStyleName(StyleResource.INSTANCE.layout().selected());
        demands.removeStyleName(StyleResource.INSTANCE.layout().selected());
        suppliers.setStyleName(StyleResource.INSTANCE.layout().selected());
        createSupplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createDemand.removeStyleName(StyleResource.INSTANCE.layout().selected());
        page1.removeStyleName(StyleResource.INSTANCE.layout().selected());
        page2.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    @UiHandler("createSupplier")
    public void onClickCreateSupplier(ClickEvent e) {
        presenter.goToCreateSupplier();
        home.removeStyleName(StyleResource.INSTANCE.layout().selected());
        demands.removeStyleName(StyleResource.INSTANCE.layout().selected());
        suppliers.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createSupplier.setStyleName(StyleResource.INSTANCE.layout().selected());
        createDemand.removeStyleName(StyleResource.INSTANCE.layout().selected());
        page1.removeStyleName(StyleResource.INSTANCE.layout().selected());
        page2.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    @UiHandler("createDemand")
    public void onClickCreateDemand(ClickEvent e) {
        presenter.goToCreateDemand();
        home.removeStyleName(StyleResource.INSTANCE.layout().selected());
        demands.removeStyleName(StyleResource.INSTANCE.layout().selected());
        suppliers.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createSupplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createDemand.setStyleName(StyleResource.INSTANCE.layout().selected());
        page1.removeStyleName(StyleResource.INSTANCE.layout().selected());
        page2.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    // TODO Praso - toto je len ukazka pre Jarinka ako treba pracovat s Menu tlacitkami
    @UiHandler("page1")
    public void onClickPage1(ClickEvent e) {
        presenter.goToPage1();
        home.removeStyleName(StyleResource.INSTANCE.layout().selected());
        demands.removeStyleName(StyleResource.INSTANCE.layout().selected());
        suppliers.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createSupplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createDemand.removeStyleName(StyleResource.INSTANCE.layout().selected());
        page1.setStyleName(StyleResource.INSTANCE.layout().selected());
        page2.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    // TODO Praso - toto je len ukazka pre Jarinka ako treba pracovat s Menu tlacitkami
    @UiHandler("page2")
    public void onClickPage2(ClickEvent e) {
        presenter.goToPage2();
        home.removeStyleName(StyleResource.INSTANCE.layout().selected());
        demands.removeStyleName(StyleResource.INSTANCE.layout().selected());
        suppliers.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createSupplier.removeStyleName(StyleResource.INSTANCE.layout().selected());
        createDemand.removeStyleName(StyleResource.INSTANCE.layout().selected());
        page1.removeStyleName(StyleResource.INSTANCE.layout().selected());
        page2.setStyleName(StyleResource.INSTANCE.layout().selected());
    }
}
