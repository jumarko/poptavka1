/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.supplierdemands;

import com.eprovement.poptavka.client.main.Constants;
import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

@Presenter(view = SupplierDemandsView.class)
public class SupplierDemandsPresenter
        extends LazyPresenter<SupplierDemandsPresenter.SupplierDemandsLayoutInterface, SupplierDemandsEventBus> {

    public interface SupplierDemandsLayoutInterface extends LazyView, IsWidget {

        Button getSupNewDemandsButton();

        Button getSupOffersButton();

        Button getSupAssignedDemandsButton();

        Button getSupCreateDemand();

        Button getSupCreateSupplier();

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
        // nothing
    }

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        view.getSupNewDemandsButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToSupplierDemandsModule(null, Constants.DEMANDS_SUPPLIER_MY_DEMANDS);
            }
        });
        view.getSupOffersButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToSupplierDemandsModule(null, Constants.DEMANDS_SUPPLIER_OFFERS);
            }
        });
        view.getSupAssignedDemandsButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToSupplierDemandsModule(null, Constants.DEMANDS_SUPPLIER_ASSIGNED_DEMANDS);
            }
        });
        view.getSupCreateDemand().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToCreateDemandModule();
            }
        });
        view.getSupCreateSupplier().addClickHandler(new ClickHandler() {

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
            case Constants.DEMANDS_SUPPLIER_MY_DEMANDS:
                eventBus.initSupplierProjects(filter);
                break;
            case Constants.DEMANDS_SUPPLIER_OFFERS:
                eventBus.initSupplierContests(filter);
                break;
            case Constants.DEMANDS_SUPPLIER_ASSIGNED_DEMANDS:
                eventBus.initSupplierAssignedProjects(filter);
                break;
            default:
                Storage.setCurrentlyLoadedView(Constants.NONE);
                view.setContent(new SupplierDemandsWelcomeView());
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
}