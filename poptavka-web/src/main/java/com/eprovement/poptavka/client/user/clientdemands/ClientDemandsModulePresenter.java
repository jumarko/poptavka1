/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.clientdemands;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.clientdemands.widgets.ClientAssignedDemandsPresenter;
import com.eprovement.poptavka.client.user.clientdemands.widgets.ClientDemandsPresenter;
import com.eprovement.poptavka.client.user.clientdemands.widgets.ClientOffersPresenter;
import com.eprovement.poptavka.client.user.widget.LoadingDiv;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.history.NavigationConfirmationInterface;
import com.mvp4g.client.history.NavigationEventCommand;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

@Presenter(view = ClientDemandsModuleView.class)
public class ClientDemandsModulePresenter
        extends LazyPresenter<ClientDemandsModulePresenter.ClientDemandsLayoutInterface, ClientDemandsModuleEventBus>
        implements NavigationConfirmationInterface {

    public interface ClientDemandsLayoutInterface extends LazyView, IsWidget {

        Button getClientNewDemandsButton();

        Button getClientOffersButton();

        Button getClientAssignedDemandsButton();

        Button getClientCreateDemand();

        Button getClientCreateSupplier();

        Button getAllDemands();

        Button getAllSuppliers();

        SimplePanel getContentPanel();

        IsWidget getWidgetView();
    }
    private ClientDemandsPresenter clientDemands = null;
    private ClientOffersPresenter clientOffers = null;
    private ClientAssignedDemandsPresenter clientAssigendDemands = null;
    private LoadingDiv loadingDiv = new LoadingDiv();

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

    @Override
    public void confirm(NavigationEventCommand event) {
        Window.confirm("Client Demand confirm method.");
        // nothing
    }

    public void onLoadingDivShow(String loadingMessage) {
        GWT.log("  - loading div created");
        if (loadingDiv == null) {
            loadingDiv = new LoadingDiv();
        }
        view.getContentPanel().clear();
        view.getContentPanel().getElement().appendChild(loadingDiv.getElement());
    }

    public void onLoadingDivHide() {
        GWT.log("  - loading div removed");
        if (view.getContentPanel().getElement().isOrHasChild(loadingDiv.getElement())) {
            view.getContentPanel().getElement().removeChild(loadingDiv.getElement());
        }
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
//        eventBus.setNavigationConfirmation(this);
        eventBus.loadingDivShow(Storage.MSGS.loading());
        switch (loadWidget) {
            case Constants.CLIENT_DEMANDS:
                if (clientDemands != null) {
                    eventBus.removeHandler(clientDemands);
                }
                clientDemands = eventBus.addHandler(ClientDemandsPresenter.class);
                clientDemands.onInitClientDemands(filter);
                break;
            case Constants.CLIENT_OFFERED_DEMANDS:
                if (clientOffers != null) {
                    eventBus.removeHandler(clientOffers);
                }
                clientOffers = eventBus.addHandler(ClientOffersPresenter.class);
                clientOffers.onInitClientOffers(filter);
                break;
            case Constants.CLIENT_ASSIGNED_DEMANDS:
                if (clientAssigendDemands != null) {
                    eventBus.removeHandler(clientAssigendDemands);
                }
                clientAssigendDemands = eventBus.addHandler(ClientAssignedDemandsPresenter.class);
                clientAssigendDemands.onInitClientAssignedDemands(filter);
                break;
            case Constants.CREATE_DEMAND:
                eventBus.goToCreateDemandModule();
                break;
            case Constants.CREATE_SUPPLIER:
                eventBus.goToCreateSupplierModule();
                break;
            default:
                eventBus.initClientDemandsWelcome();
                break;
        }
    }

    /**************************************************************************/
    /* Business events handled by presenter */
    /**************************************************************************/
    public void onDisplayView(IsWidget content) {
        view.getContentPanel().setWidget(content);
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
            case Constants.CREATE_DEMAND:
                view.getClientCreateDemand().setStyleName("");
                break;
            case Constants.CREATE_SUPPLIER:
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