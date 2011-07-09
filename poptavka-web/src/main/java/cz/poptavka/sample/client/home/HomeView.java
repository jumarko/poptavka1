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
    @UiField Hyperlink linkDisplaySuppliers;
    @UiField Hyperlink linkRegisterSupplier;

    @UiField SimplePanel contentHolder;

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        menuList.addClassName(StyleResource.INSTANCE.layout().homeMenu());

//        ArrayList<String> titles = new ArrayList<String>();
//        titles.add("DESCRIPTION");
//        titles.add("PRICE");
//        titles.add("DURATION");
//
//        final RadioTable radioTable = new RadioTable(titles, true, 1);
//        contentHolder.setWidget(radioTable);
//
//        ArrayList<String> item1 = new ArrayList<String>();
//        item1.add("17");
//        item1.add("");
//        item1.add("BLA BLA BLA SERVICE");
//        item1.add("1000 KC");
//        item1.add("3 MONTHS");
//        ArrayList<String> item2 = new ArrayList<String>();
//        item2.add("175");
//        item2.add("");
//        item2.add("BLA BLA BLA SERVICE +");
//        item2.add("3000 KC");
//        item2.add("6 MONTHS");
//        ArrayList<String> item3 = new ArrayList<String>();
//        item3.add("117");
//        item3.add("");
//        item3.add("BLA BLA BLA SERVICE ++");
//        item3.add("9000 KC");
//        item3.add("9 MONTHS");
//        ArrayList<String> item4 = new ArrayList<String>();
//        item4.add("117");
//        item4.add("");
//        item4.add("BLA BLA BLA SERVICE +++");
//        item4.add("11000 KC");
//        item4.add("12 MONTHS");
//
//        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
//        data.add(item1);
//        data.add(item2);
//        data.add(item3);
//        data.add(item4);
//
//        radioTable.setData(data);
//        radioTable.addClickHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(ClickEvent event) {
//                radioTable.getClickedRow(event);
//            }
//        });
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

    @Override
    public void setDisplayDemandsToken(String token) {
        linkDisplayDemands.setTargetHistoryToken(token);
    }

    @Override
    public void setDisplaySuppliersToken(String token) {
        linkDisplaySuppliers.setTargetHistoryToken(token);
    }

    @Override
    public void setRegisterSupplierToken(String token) {
        linkRegisterSupplier.setTargetHistoryToken(token);
    }

}
