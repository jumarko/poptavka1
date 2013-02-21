package com.eprovement.poptavka.client.common.search;

import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.homedemands.HomeDemandsSearchView;
import com.eprovement.poptavka.client.homesuppliers.HomeSuppliersSearchView;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;

/*
 * Musi byt mulitple = true, inak advance search views sa nebudu zobrazovat
 * (bude vyhadzovat chybu) -- len v pripade, ze budu dedit
 * SearchModulesViewInterface, co chceme, aby voly Lazy
 */
@Presenter(view = SearchModuleView.class, multiple = true)
public class SearchModulePresenter
        extends LazyPresenter<SearchModulePresenter.SearchModuleInterface, SearchModuleEventBus> {

    public interface SearchModuleInterface extends LazyView, IsWidget {

        //GETTERS - search bar items
        TextBox getSearchContent();

        Button getSearchBtn();

        Button getAdvSearchBtn();

        //GETTERS - widgets & panel
        AdvanceSearchContentView getAdvanceSearchContentView();

        Modal getPopupPanel();

        Widget getWidgetView();

        //SETTERS
        void setAttributeSelectorWidget(IsWidget attributeSearchViewWidget);
    }

    public interface AdvanceSearchContentInterface {

        //Setters
        void setCurrentViewTabName();

        void addCustomItemToSearchWhatBox(boolean addOrRemove);

        //Getters
        int getSearchWhat();

        Button getSearchBtn();

        TabLayoutPanel getTabLayoutPanel();

        HomeDemandsSearchView getDemandsAttributeSelectorWidget();

        HomeSuppliersSearchView getSuppliersAttributeSelectorWidget();

        SimplePanel getAttributeSelectorPanel();

        SimplePanel getCategorySelectorPanel();

        SimplePanel getLocalitySelectorPanel();

        SearchModuleDataHolder getSearchModuleDataHolder();

        Widget getWidgetView();
    }

    //Neviem zatial preco, ale nemoze to byt lazy, pretoze sa neinicializuci advace
    //search views.
    public interface SearchModulesViewInterface {

        ArrayList<FilterItem> getFilter();

        Widget getWidgetView();
    }

    @Override
    public void bindView() {
        this.addSearchBtnClickHandler();
        this.addAdvSearchBtnClickHandler();
        this.addTabLayoutPanelBeforeSelectionHandler();
    }

    /**************************************************************************/
    /** General Module events                                                 */
    /**************************************************************************/
    public void onStart() {
        // nothing
    }

    public void onForward() {
        // nothing
    }

    /**************************************************************************/
    /** Navigation events                                                     */
    /**************************************************************************/
    public void onGoToSearchModule() {
        GWT.log("SearchModule loaded");
    }

    /**************************************************************************/
    /** Additional events used in bind method                                 */
    /**************************************************************************/
    /**
     * Full text search.
     */
    private void addSearchBtnClickHandler() {
        view.getSearchBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!view.getSearchContent().getText().isEmpty()) {
                    SearchModuleDataHolder filter = new SearchModuleDataHolder();
                    //set search content text for full text search
                    filter.setSearchText(view.getSearchContent().getText());
                    forwardToCurrentView(filter);
                } else {
                    showPopupNoSearchCriteria();
                }
            }
        });
    }

    /**
     * Choose right method for searching in current view.
     * Appropriate RPC is called and it forwards user to appropriate view.
     */
    private void forwardToCurrentView(SearchModuleDataHolder filter) {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.HOME_SUPPLIERS_BY_DEFAULT:
                eventBus.goToHomeSuppliersModule(filter);
                break;
            default:
                eventBus.goToHomeDemandsModule(filter);
                break;
        }
    }

    /**
     * Advance search.
     */
    private void addAdvSearchBtnClickHandler() {
        view.getAdvanceSearchContentView().getSearchBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                //create and fill searching criteria holder - searchModuleDataHolder
                SearchModuleDataHolder filter = view.getAdvanceSearchContentView().getSearchModuleDataHolder();
                if (filter != null) {
                    view.getPopupPanel().hide();
                    forwardAccordingToSearchWhat(filter);
                } else {
                    showPopupNoSearchCriteria();
                }
            }
        });
    }

    /**
     * Choose right method for searching according to search what attribute
     * Appropriate RPC is called and it forwards user to appropriate view.
     */
    private void forwardAccordingToSearchWhat(SearchModuleDataHolder filter) {
        switch (view.getAdvanceSearchContentView().getSearchWhat()) {
            case 0:
                eventBus.goToHomeDemandsModule(filter);
                break;
            case 1:
                eventBus.goToHomeSuppliersModule(filter);
                break;
            default:
                /* Search what attribute can always set to search in eighter demands or suppliers.
                 * But if nieghter of those were chosen, it means "search in current view" item is
                 * now available. This item is dynamically added to menu according to current view,
                 * but only if it supports searching. Therefore if process got here,
                 * "search current view" is chosen therefore again, call appropriate RPC and view.
                 * The decision is made according to CurrentlyLoadedView flag. */
                if (Constants.getClientDemandsConstants().contains(Storage.getCurrentlyLoadedView())) {
                    eventBus.goToClientDemandsModule(filter, Storage.getCurrentlyLoadedView());
                }
                if (Constants.getSupplierDemandsConstants().contains(Storage.getCurrentlyLoadedView())) {
                    eventBus.goToSupplierDemandsModule(filter, Storage.getCurrentlyLoadedView());
                }
                if (Constants.getAdminConstants().contains(Storage.getCurrentlyLoadedView())) {
                    eventBus.goToAdminModule(filter, Storage.getCurrentlyLoadedView());
                }
                if (Constants.getMessagesConstants().contains(Storage.getCurrentlyLoadedView())) {
                    eventBus.goToMessagesModule(filter, Storage.getCurrentlyLoadedView());
                }
                break;
        }
    }

    private void addTabLayoutPanelBeforeSelectionHandler() {
        view.getAdvanceSearchContentView().getTabLayoutPanel().addBeforeSelectionHandler(
                new BeforeSelectionHandler<Integer>() {
                    @Override
                    public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
                        switch (event.getItem()) {
                            case AdvanceSearchContentView.CATEGORY_SELECTOR_WIDGET:
                                //If not yet initialized, do it
                                if (view.getAdvanceSearchContentView()
                                        .getCategorySelectorPanel().getWidget() == null) {
                                    eventBus.initCategoryWidget(
                                            view.getAdvanceSearchContentView().getCategorySelectorPanel(),
                                            Constants.WITH_CHECK_BOXES,
                                            CategoryCell.DISPLAY_COUNT_DISABLED,
                                            null);
                                }
                                view.getPopupPanel().addStyleName("height:300px");
                                break;
                            case AdvanceSearchContentView.LOCALITY_SELECTOR_WIDGET:
                                //If not yet initialized, do it
                                if (view.getAdvanceSearchContentView()
                                        .getLocalitySelectorPanel().getWidget() == null) {
                                    eventBus.initLocalityWidget(
                                            view.getAdvanceSearchContentView().getLocalitySelectorPanel(),
                                            Constants.WITH_CHECK_BOXES,
                                            CategoryCell.DISPLAY_COUNT_DISABLED,
                                            null);
                                }
                                view.getPopupPanel().addStyleName("height:300px");
                                break;
                            default:
                                view.getPopupPanel().addStyleName("height:150px");
                                break;
                        }
                    }
                });
    }

    private void showPopupNoSearchCriteria() {
        int left = view.getSearchContent().getElement().getAbsoluteLeft();
        int top = view.getSearchContent().getElement().getAbsoluteTop() + 30;
        PopupPanel popup = new PopupPanel(true);
        popup.setWidget(new Label(Storage.MSGS.searchNoSearchingCriteria()));
        popup.setPopupPosition(left, top);
        popup.show();
    }
}
