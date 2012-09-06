package com.eprovement.poptavka.client.common.search;

import com.eprovement.poptavka.client.common.search.AdvanceSearchContentPresenter.AdvanceSearchContentInterface;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;


import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.admin.searchViews.AdminInvoicesViewView;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.domain.adminModule.PaymentMethodDetail;
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

        Widget getWidgetView();

        Button getSearchBtn();

        Button getAdvSearchBtn();

        PopupPanel getPopupPanel();

        void setFilterSearchContent();

        void setAttributeSelectorWidget(IsWidget attributeSearchViewWidget);

        IsWidget getAttributeSelectorWidget();

        TextBox getSearchContent();
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
                SearchModuleDataHolder filter =
                        ((AdvanceSearchContentInterface) view.getPopupPanel()).getSearchModuleDataHolder();
                view.setFilterSearchContent();
                switch (Storage.getCurrentlyLoadedView()) {
                    case Constants.HOME_DEMANDS:
                        eventBus.goToHomeDemandsModule(filter);
                        break;
                    case Constants.HOME_SUPPLIERS:
                        eventBus.goToHomeSuppliersModule(filter);
                        break;
                    default:
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
                eventBus.initAdvanceSearchContent(view.getPopupPanel(), view.getAttributeSelectorWidget());
                view.getPopupPanel().setSize("400px", "300px");
                view.getPopupPanel().setPopupPosition(left, top);
                view.getPopupPanel().show();
            }
        });
    }
}