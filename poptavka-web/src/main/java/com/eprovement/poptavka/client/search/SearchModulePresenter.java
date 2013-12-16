package com.eprovement.poptavka.client.search;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
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
        Widget getWidgetView();
    }

    //Neviem zatial preco, ale nemoze to byt lazy, pretoze sa neinicializuci advace
    //search views.
    public interface SearchModulesViewInterface {

        ArrayList<FilterItem> getFilter();

        void clear();

        Widget getWidgetView();
    }

    /**************************************************************************/
    /** Attributes                                                            */
    /**************************************************************************/
    private boolean resetAdvancePopup = true;
    private Widget newAttributeSearchWidget;

    /**************************************************************************/
    /** Bind events                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        this.addSearchBtnClickHandler();
        this.addAdvanceSearchBtnClickHandler();
        this.addSearchContentBoxClickHandler();
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
    /** Business events                                                       */
    /**************************************************************************/
    public void onShowAdvancedSearchPopup() {
        if (resetAdvancePopup) {
            eventBus.initAdvanceSearchPopup(newAttributeSearchWidget);
            resetAdvancePopup = false;
        }
        eventBus.showAdvanceSearchPopup();
    }

    public void onShowPopupNoSearchCriteria() {
        int left = view.getSearchContent().getElement().getAbsoluteLeft();
        int top = view.getSearchContent().getElement().getAbsoluteTop() + 40;
        VerticalPanel vp = new VerticalPanel();
        vp.addStyleName(StyleResource.INSTANCE.modal().suggestModal());
        vp.add(new Label(Storage.MSGS.searchNoSearchingCriteria()));
        PopupPanel popup = new PopupPanel(true);
        popup.setWidget(vp);
        popup.setPopupPosition(left, top);
        popup.show();
    }

    public void onResetSearchBar(Widget newAttributeSearchWidget) {
        view.getSearchContent().setText(null);
        this.resetAdvancePopup = true;
        this.newAttributeSearchWidget = newAttributeSearchWidget;
    }

    /**************************************************************************/
    /** Additional events used in bind method                                 */
    /**************************************************************************/
    private void addSearchBtnClickHandler() {
        view.getSearchBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                executeSearch();
            }
        });
    }

    private void addAdvanceSearchBtnClickHandler() {
        view.getAdvSearchBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.showAdvancedSearchPopup();
            }
        });
    }

    private void addSearchContentBoxClickHandler() {
        view.getSearchContent().addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == Constants.ENTER_KEY_CODE) {
                    executeSearch();
                }
            }
        });
    }

    /**************************************************************************/
    /** Helper methods                                                        */
    /**************************************************************************/
    /**
     * Full text search.
     */
    private void executeSearch() {
        if (!view.getSearchContent().getText().isEmpty()) {
            SearchModuleDataHolder filter = SearchModuleDataHolder.getSearchModuleDataHolder();
            //set search content text for full text search
            filter.setSearchText(view.getSearchContent().getText());
            forwardToCurrentView(filter);
        } else {
            eventBus.showPopupNoSearchCriteria();
        }
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
}
