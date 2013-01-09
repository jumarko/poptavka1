package com.eprovement.poptavka.client.common.search;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class SearchModuleView extends Composite implements SearchModulePresenter.SearchModuleInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, SearchModuleView> {
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    @UiField
    Button searchBtn, advSearchBtn;
    @UiField
    TextBox searchContent;
    @UiField
    MenuBar searchWhatList;
    @UiField
    MenuItem searchWhatItem, demand, supplier;
    MenuItem custom;
    @UiField
    PopupPanel popupPanel;
    @UiField
    AdvanceSearchContentView advanceSearchContentView;
    //Holds data
    private SearchModuleDataHolder filters = new SearchModuleDataHolder();
    int menuItemsCount = 2;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));

        demand.setCommand(new Command() {

            @Override
            public void execute() {
                setAdvanceContentTabsVisibility(true, false, false);
                searchWhatItem.setText(demand.getText());
            }
        });
        supplier.setCommand(new Command() {

            @Override
            public void execute() {
                setAdvanceContentTabsVisibility(false, true, false);
                searchWhatItem.setText(supplier.getText());
            }
        });
        custom = new MenuItem("", new Command() {

            @Override
            public void execute() {
                setAdvanceContentTabsVisibility(false, false, true);
                searchWhatItem.setText(custom.getText());
            }
        });

        setAdvanceContentTabsVisibility(true, false, false);
        popupPanel.setAutoHideEnabled(true);
        popupPanel.setAnimationEnabled(true);
        //Aby sa nam nezobrazoval taky ramcek (popup bez widgetu) pri starte modulu
        //Musi to byt takto? Neda sa to urobit krajsie? (len hide nefunguje)
        popupPanel.show();
        popupPanel.hide();
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    @Override
    public void setAttributeSelectorWidget(IsWidget attributeSearchViewWidget) {
        //If attributeSearchViewWidget is not null -> current view searching is available
        //Therefore set widget to popup
        advanceSearchContentView.getAttributeSelectorPanel().setWidget(attributeSearchViewWidget);
        //and add new item to searchWhat listbox
        if (attributeSearchViewWidget == null) {
            addCustomItemToSearchWhatBox(false);
        } else {
            if (menuItemsCount == 3) {
                addCustomItemToSearchWhatBox(false);
            }
            addCustomItemToSearchWhatBox(true);
        }
    }

    /**
     * If full text filtering was chosen, stores given string to
     * SearchModuleDataHolder.
     */
    @Override
    public void setFilterSearchContent() {
        if (!searchContent.getText().isEmpty()
                && !searchContent.getText().equals(Storage.MSGS.searchContent())
                && filters.getAttributes().isEmpty()) {
            filters.setSearchText(searchContent.getText());
        }
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    // Search Bar items
    @Override
    public TextBox getSearchContent() {
        return searchContent;
    }

    @Override
    public int getSearchWhat() {
        if (searchWhatItem.getText().equals(demand.getText())) {
            return 0;
        } else if (searchWhatItem.getText().equals(supplier.getText())) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public Button getSearchBtn() {
        return searchBtn;
    }

    @Override
    public Button getAdvSearchBtn() {
        return advSearchBtn;
    }

    // Layouts & Panels
    @Override
    public PopupPanel getPopupPanel() {
        return popupPanel;
    }

    @Override
    public AdvanceSearchContentView getAdvanceSearchContentView() {
        return advanceSearchContentView;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
    /*
     * CLICK HANDLERS
     *
     * To define what action was made by user. Have to know because of acquiring
     * data from appropiate view loaded in popup window. See
     * handlerPopupPanelCloserEvent methods.
     */

    @UiHandler("searchContent")
    public void handleSearchContentFocusClick(FocusEvent event) {
        if (searchContent.getText().equals(Storage.MSGS.searchContent())) {
            searchContent.setText("");
        }
    }

    @UiHandler("searchContent")
    public void handleSearchContentBlurClick(BlurEvent event) {
        if (searchContent.getText().equals("")) {
            searchContent.setText(Storage.MSGS.searchContent());
        }
    }

    /*
     * POPUP CLOSE HANDLER
     */
    /**
     * When popup is closed. Appropiate filters are stored to
     * searchModuleDataHolder. Storing is according type of filtering performed
     * - categories, localities, attributes.
     */
    @UiHandler("popupPanel")
    public void handlerPopupPanelCloserEvent(CloseEvent<PopupPanel> event) {
        //Change adv button icon to - selected
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private void addCustomItemToSearchWhatBox(boolean addOrRemove) {
        if (addOrRemove) {
            custom.setText(getCurrentViewNameString());
            searchWhatList.addItem(custom);
            searchWhatItem.setText(getCurrentViewNameString());
            //Nechat to tu, alebo to dat do onForward v prezenteri?
            //Ja myslim, ze moze ostat tu
            setAdvanceContentTabsVisibility(false, false, true);
            advanceSearchContentView.setCurrentViewTabName();
            advanceSearchContentView.getTabLayoutPanel().selectTab(1);
        } else {
            if (menuItemsCount == 3) {
                searchWhatList.removeItem(custom);
            }
            searchWhatItem.setText(demand.getText());
            advanceSearchContentView.getTabLayoutPanel().selectTab(0);
        }
    }

    /**
     * Return i18n item name according to currently loaded view. The string is used
     * in searchWhat list box in search bar.
     * @return i18n item name
     */
    private String getCurrentViewNameString() {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.CLIENT_DEMANDS:
                return Storage.MSGS.searchInClientDemands();
            case Constants.CLIENT_DEMAND_DISCUSSIONS:
                return Storage.MSGS.searchInClientDemandsDiscussions();
            case Constants.CLIENT_OFFERED_DEMANDS:
                return Storage.MSGS.searchInClientOfferedDemands();
            case Constants.CLIENT_OFFERED_DEMAND_OFFERS:
                return Storage.MSGS.searchInClientOfferedDemandOffers();
            case Constants.CLIENT_ASSIGNED_DEMANDS:
                return Storage.MSGS.searchInClientAssignedDemands();
            case Constants.SUPPLIER_POTENTIAL_DEMANDS:
                return Storage.MSGS.searchInSuppliersPotentialDemands();
            case Constants.SUPPLIER_OFFERS:
                return Storage.MSGS.searchInSuppliersOffers();
            case Constants.SUPPLIER_ASSIGNED_DEMANDS:
                return Storage.MSGS.searchInSuppliersAssignedDemands();
            default:
                return Storage.MSGS.searchInCurrentView();
        }
    }

    private void setAdvanceContentTabsVisibility(
            boolean demandsTabVisible, boolean suppliersTabVisible, boolean currentViewTabVisible) {
        advanceSearchContentView.getTabLayoutPanel().getTabWidget(
                AdvanceSearchContentView.DEMANDS_SELECTOR_WIDGET).getParent().setVisible(demandsTabVisible);
        advanceSearchContentView.getTabLayoutPanel().getTabWidget(
                AdvanceSearchContentView.SUPPLIER_SELECTOR_WIDGET).getParent().setVisible(suppliersTabVisible);
        advanceSearchContentView.getTabLayoutPanel().getTabWidget(
                AdvanceSearchContentView.CURRENT_SELECTOR_WIDGET).getParent().setVisible(currentViewTabVisible);
    }
}