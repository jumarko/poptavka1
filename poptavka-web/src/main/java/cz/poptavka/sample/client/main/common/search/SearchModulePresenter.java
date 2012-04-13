package cz.poptavka.sample.client.main.common.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;


import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import cz.poptavka.sample.client.main.Constants;
import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.main.common.search.dataHolders.FilterItem;
import cz.poptavka.sample.client.main.common.search.views.AdminInvoicesViewView;
import cz.poptavka.sample.shared.domain.adminModule.PaymentMethodDetail;
import java.util.ArrayList;
import java.util.List;

/*
 * Musi byt mulitple = true, inak advance search views sa nebudu zobrazovat (bude vyhadzovat chybu)
 * -- len v pripade, ze budu dedit SearchModulesViewInterface, co chceme, aby voly Lazy
 */
@Presenter(view = SearchModuleView.class, multiple = true)
public class SearchModulePresenter
        extends LazyPresenter<SearchModulePresenter.SearchModuleInterface, SearchModuleEventBus> {

    public interface SearchModuleInterface extends LazyView {

        Widget getWidgetView();

        Button getSearchBtn();

        Button getAdvSearchBtn();

        PopupPanel getPopupPanel();

        void setFilterSearchContent();

        TextBox getSearchContent();

        TextBox getSearchCategory();

        TextBox getSearchLocality();

        SearchModuleDataHolder getFilters();
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
        view.getSearchCategory().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initCategoryWidget(view.getPopupPanel());
                showPopupPanel();
            }
        });
        view.getSearchLocality().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initLocalityWidget(view.getPopupPanel());
                showPopupPanel();
            }
        });
    }

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing
    }

    public void onForward() {
        // nothing
    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    public void onGoToSearchModule() {
        GWT.log("SearchModule loaded");
    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
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

    public void onSetSearchBarEnables(boolean category, boolean locality, boolean advanceBtn) {
        view.getSearchCategory().setEnabled(category);
        view.getSearchLocality().setEnabled(locality);
        view.getAdvSearchBtn().setEnabled(advanceBtn);
    }


    private void showPopupPanel() {
        int left = view.getSearchContent().getElement().getAbsoluteLeft();
        int top = view.getSearchContent().getElement().getAbsoluteTop() + 30;
        view.getPopupPanel().setPopupPosition(left, top);
        view.getPopupPanel().show();
    }

    /**************************************************************************/
    /* Additional events used in bind method                                  */
    /**************************************************************************/
    private void addSearchBtnClickHandler() {
        view.getSearchBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                view.setFilterSearchContent();
                switch (Storage.getCurrentlyLoadedView()) {
                    case Constants.HOME_DEMANDS:
                        eventBus.goToHomeDemandsModule(view.getFilters());
                        break;
                    case Constants.HOME_SUPPLIERS:
                        eventBus.goToHomeSuppliersModule(view.getFilters());
                        break;
                    default:
                        if (Constants.getDemandsConstants().contains(Storage.getCurrentlyLoadedView())) {
                            eventBus.goToDemandModule(view.getFilters(), Storage.getCurrentlyLoadedView());
                        }
                        if (Constants.getAdminConstants().contains(Storage.getCurrentlyLoadedView())) {
                            eventBus.goToAdminModule(view.getFilters(), Storage.getCurrentlyLoadedView());
                        }
                        if (Constants.getMessagesConstants().contains(Storage.getCurrentlyLoadedView())) {
                            eventBus.goToMessagesModule(view.getFilters(), Storage.getCurrentlyLoadedView());
                        }
                }
            }
        });
    }

    private void addAdvSearchBtnClickHandler() {
        view.getAdvSearchBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                showPopupPanel();
            }
        });
    }
}