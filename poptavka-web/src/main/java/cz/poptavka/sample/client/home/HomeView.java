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

//        ARRAYLIST<STRING> TITLES = NEW ARRAYLIST<STRING>();
//        TITLES.ADD("DESCRIPTION");
//        TITLES.ADD("PRICE");
//        TITLES.ADD("DURATION");
//
//        FINAL RADIOTABLE RADIOTABLE = NEW RADIOTABLE(TITLES, TRUE, 1);
//        CONTENTHOLDER.SETWIDGET(RADIOTABLE);
//
//        ARRAYLIST<STRING> ITEM1 = NEW ARRAYLIST<STRING>();
//        ITEM1.ADD("17");
//        ITEM1.ADD("");
//        ITEM1.ADD("BLA BLA BLA SERVICE");
//        ITEM1.ADD("1000 KC");
//        ITEM1.ADD("3 MONTHS");
//        ARRAYLIST<STRING> ITEM2 = NEW ARRAYLIST<STRING>();
//        ITEM2.ADD("175");
//        ITEM2.ADD("");
//        ITEM2.ADD("BLA BLA BLA SERVICE +");
//        ITEM2.ADD("3000 KC");
//        ITEM2.ADD("6 MONTHS");
//        ARRAYLIST<STRING> ITEM3 = NEW ARRAYLIST<STRING>();
//        ITEM3.ADD("117");
//        ITEM3.ADD("");
//        ITEM3.ADD("BLA BLA BLA SERVICE ++");
//        ITEM3.ADD("9000 KC");
//        ITEM3.ADD("9 MONTHS");
//        ARRAYLIST<STRING> ITEM4 = NEW ARRAYLIST<STRING>();
//        ITEM4.ADD("117");
//        ITEM4.ADD("");
//        ITEM4.ADD("BLA BLA BLA SERVICE +++");
//        ITEM4.ADD("11000 KC");
//        ITEM4.ADD("12 MONTHS");
//
//        ARRAYLIST<ARRAYLIST<STRING>> DATA = NEW ARRAYLIST<ARRAYLIST<STRING>>();
//        DATA.ADD(ITEM1);
//        DATA.ADD(ITEM2);
//        DATA.ADD(ITEM3);
//        DATA.ADD(ITEM4);
//
//        RADIOTABLE.SETDATA(DATA);
//        RADIOTABLE.ADDCLICKHANDLER(NEW CLICKHANDLER() {
//
//            @OVERRIDE
//            PUBLIC VOID ONCLICK(CLICKEVENT ARG0) {
//                RADIOTABLE.GETCLICKEDROW(ARG0);
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

    public void setDisplayDemandsToken(String token) {
        linkDisplayDemands.setTargetHistoryToken(token);
    }

    @Override
    public void setRegisterSupplierToken(String token) {
        linkRegisterSupplier.setTargetHistoryToken(token);
    }

}
