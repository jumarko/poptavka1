/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

@Presenter(view = SupplierDemandsModuleView.class)
public class SupplierDemandsModulePresenter
        extends LazyPresenter<SupplierLayoutInterface, SupplierDemandsModuleEventBus>
        implements NavigationConfirmationInterface {

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

    public void onForward() {
        eventBus.setBody(view.getWidgetView());
        eventBus.setToolbarContent("Professional Menu", view.getToolbarContent(), true);
        eventBus.resetSearchBar(null);
        eventBus.menuStyleChange(Constants.USER_SUPPLIER_MODULE);
        eventBus.updateUnreadMessagesCount();
    }

    @Override
    public void confirm(NavigationEventCommand event) {
        // nothing
    }

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        view.getSupplierNewDemandsButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.updateUnreadMessagesCount();
                eventBus.goToSupplierDemandsModule(null, Constants.SUPPLIER_POTENTIAL_DEMANDS);
            }
        });
        view.getSupplierOffersButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.updateUnreadMessagesCount();
                eventBus.goToSupplierDemandsModule(null, Constants.SUPPLIER_OFFERS);
            }
        });
        view.getSupplierAssignedDemandsButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.updateUnreadMessagesCount();
                eventBus.goToSupplierDemandsModule(null, Constants.SUPPLIER_ASSIGNED_DEMANDS);
            }
        });
        view.getSupplierClosedDemandsButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.updateUnreadMessagesCount();
                eventBus.goToSupplierDemandsModule(null, Constants.SUPPLIER_CLOSED_DEMANDS);
            }
        });
        view.getSupplierRatingsButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.updateUnreadMessagesCount();
                eventBus.goToSupplierDemandsModule(null, Constants.SUPPLIER_RATINGS);
            }
        });
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
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
    public void onDisplayView(IsWidget content) {
        view.setContent(content);
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC */
    /**************************************************************************/
    /**************************************************************************/
    /* Client Demands MENU                                                    */
    /**************************************************************************/
    public void onSupplierMenuStyleChange(int loadedWidget) {
        view.supplierMenuStyleChange(loadedWidget);
    }
}
