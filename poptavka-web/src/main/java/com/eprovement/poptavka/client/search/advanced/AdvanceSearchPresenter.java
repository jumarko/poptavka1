/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.search.advanced;

import com.eprovement.poptavka.client.catLocSelector.others.CatLocSelectorBuilder;
import com.eprovement.poptavka.client.search.SearchModuleEventBus;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.interfaces.IRootSelectors;
import com.eprovement.poptavka.client.search.SearchModulePresenter.SearchModulesViewInterface;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.SingleSplitter;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

/**
 * Advance Search presenter.
 * @author Martin Slavkovsky
 */
@Presenter(view = AdvanceSearchView.class, async = SingleSplitter.class)
public class AdvanceSearchPresenter
        extends LazyPresenter<AdvanceSearchPresenter.AdvanceSearchInterface, SearchModuleEventBus> {

    /**************************************************************************/
    /*  View interface                                                        */
    /**************************************************************************/
    public interface AdvanceSearchInterface extends LazyView {

        //Setters
        void setCurrentViewTabName();

        void setAdvanceContentTabsVisibility(
            boolean demandsTabVisible, boolean suppliersTabVisible, boolean currentViewTabVisible);

        void fillAttributes(SearchModuleDataHolder searchDataHolder);

        void addCustomItemToSearchWhatBox(boolean addOrRemove);

        //Getters
        int getSearchWhat();

        Button getSearchBtn1();

        Button getSearchBtn2();

        Button getClearBtn();

        Button getCloseBtn();

        TabLayoutPanel getTabLayoutPanel();

        SimplePanel getAttributeSelectorPanel();

        SimplePanel getCategorySelectorPanel();

        SimplePanel getLocalitySelectorPanel();

        AdvanceSearchView getWidgetView();
    }

    /**************************************************************************/
    /*  Bind handlers                                                         */
    /**************************************************************************/
    /**
     * Binds handlers:
     * <ul>
     *   <li>search button handler,</li>
     *   <li>tabLayout before selection handler</li>
     * </ul>
     */
    @Override
    public void bindView() {
        this.addSearchBtnClickHandler();
        this.addTabLayoutPanelBeforeSelectionHandler();
    }

    /**************************************************************************/
    /*  Attributes                                                            */
    /**************************************************************************/
    private IRootSelectors animation = GWT.create(IRootSelectors.class);
    private static final int FADE_ANIMATION_TIME = 200;
    private Timer searchBtnDisplay = new Timer() {

        @Override
        public void run() {
            animation.getCloseBtn().fadeIn(FADE_ANIMATION_TIME, null);
            animation.getClearBtn().fadeIn(FADE_ANIMATION_TIME, null);
            animation.getSearchBtn2().fadeIn(FADE_ANIMATION_TIME, null);
        }
    };

    /**************************************************************************/
    /** Navigation events                                                     */
    /**************************************************************************/
    /**
     * Inits adnvace search popup.
     * @param newAttributeSearchWidget - attribute search widget (not supported yet)
     */
    public void onInitAdvanceSearchPopup(Widget newAttributeSearchWidget) {
        switch(Storage.getCurrentlyLoadedView()) {
            case Constants.HOME_DEMANDS_MODULE:
            case Constants.HOME_DEMANDS_BY_DEFAULT:
            case Constants.HOME_DEMANDS_BY_SEARCH:
            case Constants.HOME_DEMANDS_BY_WELCOME:
                view.setAdvanceContentTabsVisibility(true, false, false);
                view.getTabLayoutPanel().selectTab(AdvanceSearchView.DEMANDS_SELECTOR_WIDGET);
                break;
            case Constants.HOME_SUPPLIERS_MODULE:
            case Constants.HOME_SUPPLIERS_BY_DEFAULT:
            case Constants.HOME_SUPPLIERS_BY_SEARCH:
                view.setAdvanceContentTabsVisibility(false, true, false);
                view.getTabLayoutPanel().selectTab(AdvanceSearchView.SUPPLIER_SELECTOR_WIDGET);
                break;
            default:
                //TODO LATER Martin 22.4.2013 - disbaled searching in current view for BETA version
                //view.getAttributeSelectorPanel().setWidget(newAttributeSearchWidget);
                //view.addCustomItemToSearchWhatBox(newAttributeSearchWidget != null);
                view.setAdvanceContentTabsVisibility(true, false, false);
                view.getTabLayoutPanel().selectTab(AdvanceSearchView.DEMANDS_SELECTOR_WIDGET);
                break;
        }
    }

    /**
     * Show popup.
     */
    public void onShowAdvanceSearchPopup() {
        view.getWidgetView().show();
    }

    /**************************************************************************/
    /** Additional events used in bind method                                 */
    /**************************************************************************/
    /**
     * Full text search.
     */
    private void executeAdvancedSearch() {
        //create and fill searching criteria holder - searchModuleDataHolder
        SearchModuleDataHolder filter = SearchModuleDataHolder.getSearchModuleDataHolder();
        fillSearchCriteria(filter);

        if (filter.getCategories().isEmpty() && filter.getLocalities().isEmpty() && filter.getAttributes().isEmpty()) {
            eventBus.showPopupNoSearchCriteria();
        } else {
            view.getWidgetView().hide();
            forwardAccordingToSearchWhat(filter);
        }
    }

    /**
     * Fills search criteria
     * @param filter - search criteria that will be updated
     */
    private void fillSearchCriteria(SearchModuleDataHolder filter) {
        //fill attributes
        view.fillAttributes(filter);
        //fill categories
        eventBus.fillCatLocs(filter.getCategories(), Constants.HOME_SEARCH_MODULE);
        //fill localities
        eventBus.fillCatLocs(filter.getLocalities(), -Constants.HOME_SEARCH_MODULE);
    }

    /**
     * Binds search & close buttons handlers.
     */
    private void addSearchBtnClickHandler() {
        view.getSearchBtn1().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                executeAdvancedSearch();
            }
        });
        view.getSearchBtn2().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                executeAdvancedSearch();
            }
        });
        view.getClearBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Widget visibleWidget = view.getTabLayoutPanel().getWidget(view.getTabLayoutPanel().getSelectedIndex());
                ((SearchModulesViewInterface) visibleWidget).clear();
            }
        });
        view.getCloseBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                view.getWidgetView().hide();
            }
        });
    }

    /**
     * Choose right method for searching according to search what attribute
     * Appropriate RPC is called and it forwards user to appropriate view.
     */
    private void forwardAccordingToSearchWhat(SearchModuleDataHolder filter) {
        switch (view.getSearchWhat()) {
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

    /**
     * Binds tab layout before selection handler.
     * Initialize widgets before selecting tab.
     */
    private void addTabLayoutPanelBeforeSelectionHandler() {
        view.getTabLayoutPanel().addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
            @Override
            public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
                hideSearchBtns(1000);
                switch (event.getItem()) {
                    case AdvanceSearchView.CATEGORY_SELECTOR_WIDGET:
//                        animation.getAdvanceSearchPopup().animate("max-width: 1075px", null);
                        //If not yet initialized, do it
                        if (view.getCategorySelectorPanel().getWidget() == null) {
                            eventBus.initCatLocSelector(
                                    view.getCategorySelectorPanel(),
                                    new CatLocSelectorBuilder.Builder(Constants.HOME_SEARCH_MODULE)
                                        .initCategorySelector()
                                        .initSelectorManager()
                                        .withCheckboxes()
                                        .setSelectionRestriction(Constants.REGISTER_MAX_CATEGORIES)
                                        .build());
                        }
                        break;
                    case AdvanceSearchView.LOCALITY_SELECTOR_WIDGET:
//                        animation.getAdvanceSearchPopup().animate("max-width: 1075px", null);
                        //If not yet initialized, do it
                        if (view.getLocalitySelectorPanel().getWidget() == null) {
                            eventBus.initCatLocSelector(
                                    view.getLocalitySelectorPanel(),
                                    new CatLocSelectorBuilder.Builder(Constants.HOME_SEARCH_MODULE)
                                        .initLocalitySelector()
                                        .initSelectorManager()
                                        .withCheckboxes()
                                        .setSelectionRestriction(Constants.REGISTER_MAX_LOCALITIES)
                                        .build());
                        }
                        break;
                    default:
//                        animation.getAdvanceSearchPopup().animate("max-width: 500px", null);
                        break;
                }
            }
        });
    }

    /**
     * Hides search buttons for a given period of time.
     * @param millis define time in millisecond
     */
    private void hideSearchBtns(int millis) {
        animation.getCloseBtn().fadeOut(FADE_ANIMATION_TIME, null);
        animation.getClearBtn().fadeOut(FADE_ANIMATION_TIME, null);
        animation.getSearchBtn2().fadeOut(FADE_ANIMATION_TIME, null);
        searchBtnDisplay.schedule(millis);
    }
}