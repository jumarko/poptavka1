package cz.poptavka.sample.client.home;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.resources.StyleResource;

/**
 * Main view for home/unlogged user aka public section.
 *
 * @author Beho
 *
 */
public class HomeView extends Composite implements HomePresenter.HomeInterface {

    private static HomeViewUiBinder uiBinder = GWT.create(HomeViewUiBinder.class);

    interface HomeViewUiBinder extends UiBinder<Widget, HomeView> {
    }

    @UiField HTMLPanel container;
    //menu section

    @UiField UListElement menuList;
    @UiField Hyperlink linkHome;
    @UiField Hyperlink linkCreateDemand;
    @UiField Hyperlink linkDisplayDemands;
    @UiField Hyperlink linkRegisterSupplier;

    @UiField SimplePanel contentHolder;

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        menuList.addClassName(StyleResource.INSTANCE.layout().homeMenu());
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    public void setBody(Widget body) {
        contentHolder.setWidget(body);
    }

    @Override
    public void setCreateDemandToken(String token) {
        linkCreateDemand.setTargetHistoryToken(token);
    }

    public void setHomeToken(String token) {
        linkHome.setTargetHistoryToken(token);
    }

    public void setDisplayDemandsToken(String token) {
        linkDisplayDemands.setTargetHistoryToken(token);
    }

    @Override
    public void setRegisterSupplierToken(String token) {
        linkRegisterSupplier.setTargetHistoryToken(token);
    }

}
