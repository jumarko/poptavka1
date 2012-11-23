/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.clientdemands;

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

@Presenter(view = ClientDemandsModuleView.class)
public class ClientDemandsModulePresenter
        extends LazyPresenter<ClientDemandsModulePresenter.ClientDemandsLayoutInterface, ClientDemandsModuleEventBus> {

    public interface ClientDemandsLayoutInterface extends LazyView, IsWidget {

        Button getClientNewDemandsButton();

        Button getClientOffersButton();

        Button getClientAssignedDemandsButton();

        Button getClientCreateDemand();

        Button getClientCreateSupplier();

        Button getAllDemands();

        Button getAllSuppliers();

        void setContent(IsWidget contentWidget);

        IsWidget getWidgetView();
    }

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing
    }

    public void onForward() {
        //Must be set before any widget start initialize because of autoDisplay feature
        Storage.setCurrentlyLoadedView(Constants.USER_CLIENT_MODULE);
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
        view.getClientNewDemandsButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToClientDemandsModule(null, Constants.CLIENT_DEMANDS);
            }
        });
        view.getClientOffersButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToClientDemandsModule(null, Constants.CLIENT_OFFERED_DEMANDS);
            }
        });
        view.getClientAssignedDemandsButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToClientDemandsModule(null, Constants.CLIENT_ASSIGNED_DEMANDS);
            }
        });
        view.getClientCreateDemand().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToCreateDemandModule();
            }
        });
        view.getClientCreateSupplier().addClickHandler(new ClickHandler() {
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
    public void onGoToClientDemandsModule(SearchModuleDataHolder filter, int loadWidget) {
        switch (loadWidget) {
            case Constants.CLIENT_DEMANDS:
                eventBus.initClientDemands(filter);
                break;
            case Constants.CLIENT_OFFERED_DEMANDS:
                eventBus.initClientOffers(filter);
                break;
            case Constants.CLIENT_ASSIGNED_DEMANDS:
                eventBus.initClientAssignedDemands(filter);
                break;
            case Constants.HOME_CREATE_DEMAND:
                eventBus.goToCreateDemandModule();
                break;
            case Constants.HOME_CREATE_SUPPLIERS:
                eventBus.goToCreateSupplierModule();
                break;
            default:
                Storage.setCurrentlyLoadedView(Constants.CLIENT_DEMANDS_WELCOME);
                eventBus.displayView(new ClientDemandsModuleWelcomeView());
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
    public void onSelectClientDemandsMenu(int loadedWidget) {
        //TODO Martin null replace with CSS style - e.g. Storage.RSCS.clientDemandsButtonSelected()
        switch (loadedWidget) {
            case Constants.CLIENT_DEMANDS:
                view.getClientCreateDemand().setStyleName("");
                break;
            case Constants.CLIENT_OFFERED_DEMANDS:
                view.getClientOffersButton().setStyleName("");
                break;
            case Constants.CLIENT_ASSIGNED_DEMANDS:
                view.getClientAssignedDemandsButton().setStyleName("");
                break;
            case Constants.HOME_CREATE_DEMAND:
                view.getClientCreateDemand().setStyleName("");
                break;
            case Constants.HOME_CREATE_SUPPLIERS:
                view.getClientCreateSupplier().setStyleName("");
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