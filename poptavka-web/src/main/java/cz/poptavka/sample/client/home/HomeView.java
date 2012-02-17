package cz.poptavka.sample.client.home;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * Main view for home/unlogged user aka public section.
 *
 * @author Beho
 *
 */
public class HomeView extends Composite implements HomePresenter.HomeInterface {

//    private static HomeViewUiBinder uiBinder = GWT.create(HomeViewUiBinder.class);
//    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    interface HomeViewUiBinder extends UiBinder<Widget, HomeView> {
    }
    @UiField
    HTMLPanel container;
//    //menu section
//    @UiField
//    UListElement menuList;
//    @UiField
//    Hyperlink linkHome;
//    @UiField
//    Button demands;
//    @UiField
//    Button suppliers;
//    @UiField
//    Button createSupplier;
//    @UiField
//    Button createDemand;
//    @UiField
//    SimplePanel contentHolder, searchPanel;
//    @UiField
//    SearchModuleView searchView;
//    @UiField
//    AdvancedSearchView advancedSearchView;
//    @UiField
//    DockLayoutPanel homeLayoutHolder;

    @Override
    public void createView() {
//        initWidget(uiBinder.createAndBindUi(this));
//        menuList.addClassName(StyleResource.INSTANCE.layout().homeMenu());
//        advancedSearchView.setVisible(false);
    }

//    @Override
//    public SearchModuleView getSearchView() {
//        return searchView;
//    }
//
//    @Override
//    public AdvancedSearchView getAdvancedSearchView() {
//        return advancedSearchView;
//    }
//    @Override
//    public Widget getWidgetView() {
//        return this;
//    }
//
//    @Override
//    public void setBody(Widget body) {
//        contentHolder.setWidget(body);
//    }
//
//    @Override
//    public SimplePanel getSearchPanel() {
//        return searchPanel;
//    }
//
//    public void setHomeToken(String token) {
//        linkHome.setTargetHistoryToken(token);
//    }
//
//    @Override
//    public HasClickHandlers getDemandsButton() {
//        return demands;
//    }
//
//    @Override
//    public HasClickHandlers getSuppliersButton() {
//        return suppliers;
//    }
//
//    @Override
//    public HasClickHandlers getCreateSupplierButton() {
//        return createSupplier;
//    }
//
//    @Override
//    public HasClickHandlers getCreateDemandButton() {
//        return createDemand;
//    }
}
