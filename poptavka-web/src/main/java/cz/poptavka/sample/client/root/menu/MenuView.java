package cz.poptavka.sample.client.root.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Hyperlink;
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
    Hyperlink linkHome;
    @UiField
    Button demands, suppliers, createSupplier, createDemand;
    @UiField
    UListElement menuList;

    public MenuView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public MenuView(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
        menuList.addClassName(StyleResource.INSTANCE.layout().homeMenu());
    }

    @Override
    public HasClickHandlers getDemandsButton() {
        return demands;
    }

    @Override
    public HasClickHandlers getSuppliersButton() {
        return suppliers;
    }

    @Override
    public HasClickHandlers getCreateSupplierButton() {
        return createSupplier;
    }

    @Override
    public HasClickHandlers getCreateDemandButton() {
        return createDemand;
    }

    @Override
    public void setHomeToken(String token) {
        linkHome.setTargetHistoryToken(token);
    }
}
