/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.supplierdemands;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.root.toolbar.ProvidesToolbar;
import com.eprovement.poptavka.client.user.supplierdemands.SupplierDemandsModulePresenter.SupplierLayoutInterface;
import com.eprovement.poptavka.client.user.supplierdemands.toolbar.SupplierToolbarView;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.history.NavigationConfirmationInterface;
import com.mvp4g.client.history.NavigationEventCommand;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

/**
 * SupplierDemands module presenter.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = SupplierDemandsModuleView.class)
public class SupplierDemandsModulePresenter
        extends LazyPresenter<SupplierLayoutInterface, SupplierDemandsModuleEventBus>
        implements NavigationConfirmationInterface {

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
    public interface SupplierLayoutInterface extends LazyView, IsWidget, ProvidesToolbar {

        Button getSupplierNewDemandsButton();

        Button getSupplierOffersButton();

        Button getSupplierAssignedDemandsButton();

        Button getSupplierClosedDemandsButton();

        Button getSupplierRatingsButton();

        void setContent(IsWidget contentWidget);

        void supplierMenuStyleChange(int loadedWidget);

        IsWidget getWidgetView();
    }

    /**************************************************************************/
    /* General Module events */
    /**************************************************************************/
    public void onStart() {
        // nothing by default
    }

    /**
     * Sets body, toolbar, searchbar and update unread messages count on each forward.
     */
    public void onForward() {
        eventBus.setBody(view.getWidgetView());
        eventBus.setToolbarContent("Professional Menu", view.getToolbarContent(), true);
        eventBus.resetSearchBar(null);
        eventBus.menuStyleChange(Constants.USER_SUPPLIER_MODULE);
        eventBus.updateUnreadMessagesCount();
    }

    @Override
    public void confirm(NavigationEventCommand event) {
        // nothing by default
    }

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    /**
     * Bind menu buttons handlers.
     */
    @Override
    public void bindView() {
        view.getSupplierNewDemandsButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.closeSubMenu();
                eventBus.updateUnreadMessagesCount();
                eventBus.goToSupplierDemandsModule(null, Constants.SUPPLIER_POTENTIAL_DEMANDS);
            }
        });
        view.getSupplierOffersButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.closeSubMenu();
                eventBus.updateUnreadMessagesCount();
                eventBus.goToSupplierDemandsModule(null, Constants.SUPPLIER_OFFERS);
            }
        });
        view.getSupplierAssignedDemandsButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.closeSubMenu();
                eventBus.updateUnreadMessagesCount();
                eventBus.goToSupplierDemandsModule(null, Constants.SUPPLIER_ASSIGNED_DEMANDS);
            }
        });
        view.getSupplierClosedDemandsButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.closeSubMenu();
                eventBus.updateUnreadMessagesCount();
                eventBus.goToSupplierDemandsModule(null, Constants.SUPPLIER_CLOSED_DEMANDS);
            }
        });
        view.getSupplierRatingsButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.closeSubMenu();
                eventBus.updateUnreadMessagesCount();
                eventBus.goToSupplierDemandsModule(null, Constants.SUPPLIER_RATINGS);
            }
        });
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    /**
     * Created SupplierDemands module.
     * @param filter - search criteria
     * @param loadWidget - widget id
     */
    public void onGoToSupplierDemandsModule(SearchModuleDataHolder filter, int loadWidget) {
        ((SupplierToolbarView) view.getToolbarContent()).resetBasic();
        switch (loadWidget) {
            case Constants.SUPPLIER_POTENTIAL_DEMANDS:
                eventBus.initSupplierDemands(filter);
                break;
            case Constants.SUPPLIER_OFFERS:
                eventBus.initSupplierOffers(filter);
                break;
            case Constants.SUPPLIER_ASSIGNED_DEMANDS:
                eventBus.initSupplierAssignedDemands(filter);
                break;
            case Constants.SUPPLIER_CLOSED_DEMANDS:
                eventBus.initSupplierClosedDemands(filter);
                break;
            case Constants.SUPPLIER_RATINGS:
                eventBus.initSupplierRatings(filter);
                break;
            default:
                ((SupplierToolbarView) view.getToolbarContent()).resetFull();
                eventBus.initSupplierDemandsWelcome();
                break;
        }
    }

    /**************************************************************************/
    /* Business events handled by presenter */
    /**************************************************************************/
    /**
     * Displays SupplierModule's widget in content area.
     * @param content container
     */
    public void onDisplayView(IsWidget content) {
        view.setContent(content);
    }

    /**************************************************************************/
    /* Client Demands MENU                                                    */
    /**************************************************************************/
    /**
     * Sets active style to menu buttons styles.
     * @param loadedWidget id
     */
    public void onSupplierMenuStyleChange(int loadedWidget) {
        view.supplierMenuStyleChange(loadedWidget);
    }
}
