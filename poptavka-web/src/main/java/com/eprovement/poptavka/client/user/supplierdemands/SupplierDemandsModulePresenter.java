/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.supplierdemands;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

@Presenter(view = SupplierDemandsModuleView.class)
public class SupplierDemandsModulePresenter extends LazyPresenter<
        SupplierDemandsModulePresenter.SupplierDemandsLayoutInterface, SupplierDemandsModuleEventBus> {

    public interface SupplierDemandsLayoutInterface extends LazyView, IsWidget {

        Button getSupplierNewDemandsButton();

        Button getSupplierOffersButton();

        Button getSupplierAssignedDemandsButton();

        Button getSupplierCreateDemand();

        Button getSupplierCreateSupplier();

        Button getAllDemands();

        Button getAllSuppliers();

        void setContent(IsWidget contentWidget);

        IsWidget getWidgetView();
    }

    /**************************************************************************/
    /* General Module events */
    /**************************************************************************/
    public void onStart() {
        // nothing
    }

    public void onForward() {
        if (!(Storage.getUser() == null && Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL())) {
            eventBus.updateUnreadMessagesCount();
        }
        eventBus.setUpSearchBar(null);
    }

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        view.getSupplierNewDemandsButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToSupplierDemandsModule(null, Constants.SUPPLIER_POTENTIAL_DEMANDS);
            }
        });
        view.getSupplierOffersButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToSupplierDemandsModule(null, Constants.SUPPLIER_OFFERS);
            }
        });
        view.getSupplierAssignedDemandsButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToSupplierDemandsModule(null, Constants.SUPPLIER_ASSIGNED_DEMANDS);
            }
        });
        view.getSupplierCreateDemand().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToCreateDemandModule();
            }
        });
        view.getSupplierCreateSupplier().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToCreateSupplierModule();
            }
        });
        view.getAllDemands().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToHomeDemandsModule(null);
            }
        });
        view.getAllSuppliers().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToHomeSuppliersModule(null);
            }
        });
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    public void onGoToSupplierDemandsModule(SearchModuleDataHolder filter, int loadWidget) {
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
            default:
                Storage.setCurrentlyLoadedView(Constants.NONE);
                eventBus.displayView(new SupplierDemandsModuleWelcomeView());
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
    public void onSelectSupplierDemandsMenu(int loadedWidget) {
        //TODO Martin null replace with CSS style - e.g. Storage.RSCS.clientDemandsButtonSelected()
        switch (loadedWidget) {
            case Constants.SUPPLIER_POTENTIAL_DEMANDS:
                view.getSupplierNewDemandsButton().setStyleName("");
                break;
            case Constants.SUPPLIER_OFFERS:
                view.getSupplierOffersButton().setStyleName("");
                break;
            case Constants.SUPPLIER_ASSIGNED_DEMANDS:
                view.getSupplierAssignedDemandsButton().setStyleName("");
                break;
            case Constants.HOME_CREATE_DEMAND:
                view.getSupplierCreateDemand().setStyleName("");
                break;
            case Constants.HOME_CREATE_SUPPLIERS:
                view.getSupplierCreateSupplier().setStyleName("");
                break;
            case Constants.HOME_DEMANDS_MODULE:
                view.getAllDemands().setStyleName("");
                break;
            case Constants.HOME_SUPPLIERS_MODULE:
                view.getAllSuppliers().setStyleName("");
                break;
            default:
                break;
        }
    }
}