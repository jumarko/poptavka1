/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.search.advanced;

import com.eprovement.poptavka.client.search.SearchModulePresenter.SearchModulesViewInterface;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.homedemands.HomeDemandsSearchView;
import com.eprovement.poptavka.client.homesuppliers.HomeSuppliersSearchView;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Advance Search view consists of tabLayout panel with
 * <b>DemandAttribute, SupplierAttribute, CurrectAttribute, CategorySelector, LocalitySelector</b> tabs.
 * @author Martin Slavkovsky
 */
public class AdvanceSearchView extends Modal
        implements AdvanceSearchPresenter.AdvanceSearchInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static AdvanceSearchContentViewUiBinder uiBinder = GWT.create(AdvanceSearchContentViewUiBinder.class);

    interface AdvanceSearchContentViewUiBinder extends UiBinder<Widget, AdvanceSearchView> {
    }

    /**************************************************************************/
    /* CSS                                                                    */
    /**************************************************************************/
    static {
        CssInjector.INSTANCE.ensureModalStylesInjected();
        CssInjector.INSTANCE.ensureCommonStylesInjected();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField Label currentViewAttributesLabel;
    @UiField Button searchBtn1, searchBtn2, clearBtn, closeBtn;
    @UiField TabLayoutPanel mainPanel;
    @UiField SimplePanel attributeSelectorWidgetPanel, categorySelectorWidgetPanel, localitySelectorWidgetPanel;
    @UiField MenuBar searchWhatList;
    @UiField MenuItem searchWhatItem, demand, supplier;
    @UiField HomeDemandsSearchView demandsAttributeSelectorWidget;
    @UiField HomeSuppliersSearchView suppliersAttributeSelectorWidget;
    /** Class attributes. **/
    private MenuItem custom;
    private int menuItemsCount = 2;
    /** Constants. **/
    public static final int DEMANDS_SELECTOR_WIDGET = 0;
    public static final int SUPPLIER_SELECTOR_WIDGET = 1;
    public static final int CURRENT_SELECTOR_WIDGET = 2;
    public static final int CATEGORY_SELECTOR_WIDGET = 3;
    public static final int LOCALITY_SELECTOR_WIDGET = 4;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates AdvanceSearch view's compotents.
     */
    @Override
    public void createView() {
        add(uiBinder.createAndBindUi(this));

        bindHandlers();

        addStyleName(StyleResource.INSTANCE.modal().advancedSearchPopup());
        setId("gwt-debug-advanceSearchPopup");
        setAnimation(true);
        setDynamicSafe(true);
    }

    /**
     * Bind handlers.
     * Places here, because this is only UI functionality to set tabs visibility
     * according to <b>searchWhat</b> dropdown menu choice.
     */
    public void bindHandlers() {
        demand.setScheduledCommand(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                mainPanel.selectTab(AdvanceSearchView.DEMANDS_SELECTOR_WIDGET);
                setAdvanceContentTabsVisibility(true, false, false);
                searchWhatItem.setText(demand.getText());
            }
        });
        supplier.setScheduledCommand(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                mainPanel.selectTab(AdvanceSearchView.SUPPLIER_SELECTOR_WIDGET);
                setAdvanceContentTabsVisibility(false, true, false);
                searchWhatItem.setText(supplier.getText());
            }
        });
        custom = new MenuItem("", new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                mainPanel.selectTab(AdvanceSearchView.CURRENT_SELECTOR_WIDGET);
                setAdvanceContentTabsVisibility(false, false, true);
                searchWhatItem.setText(custom.getText());
            }
        });
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    /**
     * Sets current tab's name according to current loaded view (currentAttribute widget).
     */
    @Override
    public void setCurrentViewTabName() {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.CLIENT_DEMANDS:
                currentViewAttributesLabel.setText(Storage.MSGS.searchClientDemandsTab());
                break;
            case Constants.CLIENT_DEMAND_DISCUSSIONS:
                currentViewAttributesLabel.setText(Storage.MSGS.searchClientDemandsDiscussionsTab());
                break;
            case Constants.CLIENT_OFFERED_DEMANDS:
                currentViewAttributesLabel.setText(Storage.MSGS.searchClientOfferedDemandsTab());
                break;
            case Constants.CLIENT_OFFERED_DEMAND_OFFERS:
                currentViewAttributesLabel.setText(Storage.MSGS.searchClientOfferedDemandOffersTab());
                break;
            case Constants.CLIENT_ASSIGNED_DEMANDS:
                currentViewAttributesLabel.setText(Storage.MSGS.searchClientAssignedDemandsTab());
                break;
            case Constants.SUPPLIER_POTENTIAL_DEMANDS:
                currentViewAttributesLabel.setText(Storage.MSGS.searchSuppliersPotentialDemandsTab());
                break;
            case Constants.SUPPLIER_OFFERS:
                currentViewAttributesLabel.setText(Storage.MSGS.searchSuppliersOffersTab());
                break;
            case Constants.SUPPLIER_ASSIGNED_DEMANDS:
                currentViewAttributesLabel.setText(Storage.MSGS.searchSuppliersAssignedDemandsTab());
                break;
            default:
                currentViewAttributesLabel.setText(Storage.MSGS.searchCurrentViewTab());
                break;
        }
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * @return the custom attribute selector panel
     */
    @Override
    public SimplePanel getAttributeSelectorPanel() {
        return attributeSelectorWidgetPanel;
    }

    /**
     * @return the category selector panel
     */
    @Override
    public SimplePanel getCategorySelectorPanel() {
        return categorySelectorWidgetPanel;
    }

    /**
     * @return the locality selector panel
     */
    @Override
    public SimplePanel getLocalitySelectorPanel() {
        return localitySelectorWidgetPanel;
    }

    /**
     * @return the tab layout panel
     */
    @Override
    public TabLayoutPanel getTabLayoutPanel() {
        return mainPanel;
    }

    /**
     * @return the search button 1
     */
    @Override
    public Button getSearchBtn1() {
        return searchBtn1;
    }

    /**
     * @return the search button 2
     */
    @Override
    public Button getSearchBtn2() {
        return searchBtn2;
    }

    /**
     * @return the clear button
     */
    @Override
    public Button getClearBtn() {
        return clearBtn;
    }

    /**
     * @return the close button
     */
    @Override
    public Button getCloseBtn() {
        return closeBtn;
    }

    /**
     * @return the search what choice
     */
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

    /**
     * Fills search criteria's attributes
     * @param searchDataHolder - search criteria object that is to be updated
     */
    @Override
    public void fillAttributes(SearchModuleDataHolder searchDataHolder) {
        if (mainPanel.getTabWidget(DEMANDS_SELECTOR_WIDGET).getParent().isVisible()) {
            searchDataHolder.setAttributes(demandsAttributeSelectorWidget.getFilter());
        }
        if (mainPanel.getTabWidget(SUPPLIER_SELECTOR_WIDGET).getParent().isVisible()) {
            searchDataHolder.setAttributes(suppliersAttributeSelectorWidget.getFilter());
        }
        if (mainPanel.getTabWidget(CURRENT_SELECTOR_WIDGET).getParent().isVisible()) {
            searchDataHolder.setAttributes(((SearchModulesViewInterface) attributeSelectorWidgetPanel.getWidget())
                    .getFilter());
        }
    }

    /**
     * @return the widget view
     */
    @Override
    public AdvanceSearchView getWidgetView() {
        return this;
    }

    /**************************************************************************/
    /*  Helper methods                                                        */
    /**************************************************************************/
    /**
     * Sets basic tabs visibity.
     * @param demandsTabVisible true to display demandAttributeTab
     * @param suppliersTabVisible true to display supplierAttributeTab
     * @param currentViewTabVisible true to display currentAttributeTab
     */
    @Override
    public void setAdvanceContentTabsVisibility(
            boolean demandsTabVisible, boolean suppliersTabVisible, boolean currentViewTabVisible) {
        mainPanel.getTabWidget(AdvanceSearchView.DEMANDS_SELECTOR_WIDGET)
                .getParent().setVisible(demandsTabVisible);
        mainPanel.getTabWidget(AdvanceSearchView.SUPPLIER_SELECTOR_WIDGET)
                .getParent().setVisible(suppliersTabVisible);
        mainPanel.getTabWidget(AdvanceSearchView.CURRENT_SELECTOR_WIDGET)
                .getParent().setVisible(currentViewTabVisible);
    }

    /**
     * Adds choice to searchWhat dropdown menu.
     * @param addOrRemove - true to add currentWidget name to searchWhat dropdown menu,
     *                      false otherwise
     */
    @Override
    public void addCustomItemToSearchWhatBox(boolean addOrRemove) {
        if (addOrRemove) {
            custom.setText(getCurrentViewNameString());
            searchWhatList.addItem(custom);
            searchWhatItem.setText(getCurrentViewNameString());
            setAdvanceContentTabsVisibility(false, false, true);
            setCurrentViewTabName();
            mainPanel.selectTab(CURRENT_SELECTOR_WIDGET);
        } else {
            if (menuItemsCount == 3) {
                searchWhatList.removeItem(custom);
            }
            searchWhatItem.setText(demand.getText());
            setAdvanceContentTabsVisibility(true, false, false);
            mainPanel.selectTab(DEMANDS_SELECTOR_WIDGET);
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
}