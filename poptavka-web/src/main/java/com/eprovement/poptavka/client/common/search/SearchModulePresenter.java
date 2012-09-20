package com.eprovement.poptavka.client.common.search;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.homedemands.HomeDemandsSearchView;
import com.eprovement.poptavka.client.homesuppliers.HomeSuppliersSearchView;
import com.eprovement.poptavka.client.user.admin.searchViews.AdminInvoicesViewView;
import com.eprovement.poptavka.shared.domain.adminModule.PaymentMethodDetail;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;
import java.util.List;

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

        ListBox getSearchWhat();

        Button getSearchBtn();

        Button getAdvSearchBtn();

        //GETTERS - widgets & panel
        AdvanceSearchContentView getAdvanceSearchContentView();

        PopupPanel getPopupPanel();

        Widget getWidgetView();

        //SETTERS
        void setFilterSearchContent();

        void setAttributeSelectorWidget(IsWidget attributeSearchViewWidget);
    }

    public interface AdvanceSearchContentInterface {

        //Setters
        void setCurrentViewTabName();

        //Getters
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
    /** Business events handled by presenter                                  */
    /**************************************************************************/
    public void onClearSearchContent() {
        view.getSearchContent().setText(Storage.MSGS.searchContent());
    }

    public void onResponsePaymentMethods(final List<PaymentMethodDetail> list) {
        final ListBox box = ((AdminInvoicesViewView) view.getPopupPanel().getWidget()).getPaymentMethodList();
        box.clear();
        box.setVisible(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                box.addItem("select method...");
                for (int i = 0; i < list.size(); i++) {
                    box.addItem(list.get(i).getName(), list.get(i).getName());
                }
                box.setSelectedIndex(0);
                GWT.log("PaymentMethodList filled");
            }
        });
    }

    /**************************************************************************/
    /** Additional events used in bind method                                 */
    /**************************************************************************/
    private void addSearchBtnClickHandler() {
        view.getSearchBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                AdvanceSearchContentInterface advSearchContent
                    = (AdvanceSearchContentInterface) view.getPopupPanel().getWidget();
                SearchModuleDataHolder filter = advSearchContent.getSearchModuleDataHolder();
                //if popup was shown, but no filters were set
                if (filter == null) {
                    showPopupNoSearchCriteria();
                    return;
                }
                view.setFilterSearchContent();
                switch (view.getSearchWhat().getSelectedIndex()) {
                    case 0:
                        eventBus.goToHomeDemandsModule(filter, Constants.HOME_DEMANDS_BY_SEARCH);
                        break;
                    case 1:
                        eventBus.goToHomeSuppliersModule(filter, Constants.HOME_SUPPLIERS_BY_SEARCH);
                        break;
                    default:
                        //if current item is available in searchWhat listbox, folowing code is invoked
                        //if not, processing don't come this far
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
        });
    }

    private void addAdvSearchBtnClickHandler() {
        view.getAdvSearchBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                int left = view.getSearchContent().getElement().getAbsoluteLeft();
                int top = view.getSearchContent().getElement().getAbsoluteTop() + 30;
                view.getPopupPanel().setSize("500px", "300px");
                view.getPopupPanel().setPopupPosition(left, top);
                view.getPopupPanel().show();
            }
        });
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
                                            Constants.WITH_CHECK_BOXES);
                                }
                                break;
                            case AdvanceSearchContentView.LOCALITY_SELECTOR_WIDGET:
                                //If not yet initialized, do it
                                if (view.getAdvanceSearchContentView()
                                        .getLocalitySelectorPanel().getWidget() == null) {
                                    eventBus.initLocalityWidget(
                                            view.getAdvanceSearchContentView().getLocalitySelectorPanel());
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
    }

    private void showPopupNoSearchCriteria() {
        int left = view.getSearchContent().getElement().getAbsoluteLeft();
        int top = view.getSearchContent().getElement().getAbsoluteTop() + 30;
        PopupPanel popup = new PopupPanel(true);
        popup.setWidget(new Label(Storage.MSGS.noSearchingCriteria()));
        popup.setPopupPosition(left, top);
        popup.show();
    }
}