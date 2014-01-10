/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.clientdemands;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.toolbar.ProvidesToolbar;
import com.eprovement.poptavka.client.user.clientdemands.ClientDemandsModulePresenter.ClientDemandsViewInterface;
import com.eprovement.poptavka.client.user.clientdemands.toolbar.ClientToolbarView;
import com.eprovement.poptavka.client.user.widget.LoadingDiv;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.history.NavigationConfirmationInterface;
import com.mvp4g.client.history.NavigationEventCommand;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

/**
 * Client Demands module presenter.
 * @author Martin Slavkovsky
 */
@Presenter(view = ClientDemandsModuleView.class)
public class ClientDemandsModulePresenter
        extends LazyPresenter<ClientDemandsViewInterface, ClientDemandsModuleEventBus>
        implements NavigationConfirmationInterface {

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
    public interface ClientDemandsViewInterface extends LazyView, IsWidget, ProvidesToolbar {

        Button getClientNewDemandsButton();

        Button getClientOffersButton();

        Button getClientAssignedDemandsButton();

        Button getClientClosedDemandsButton();

        Button getClientRatingsButton();

        SimplePanel getContentContainer();

        void clientMenuStyleChange(int loadedView);

        IsWidget getWidgetView();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private LoadingDiv loadingDiv = new LoadingDiv();

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing by default
    }

    /**
     * Sets bodu, toolbar, search and update unread messages count.
     */
    public void onForward() {
        eventBus.setBody(view.getWidgetView());
        eventBus.setToolbarContent("Client Menu", view.getToolbarContent(), true);
        eventBus.resetSearchBar(null);
        eventBus.menuStyleChange(Constants.USER_CLIENT_MODULE);
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
        view.getClientNewDemandsButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.closeSubMenu();
                eventBus.updateUnreadMessagesCount();
                eventBus.goToClientDemandsModule(null, Constants.CLIENT_DEMANDS);
            }
        });
        view.getClientOffersButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.closeSubMenu();
                eventBus.updateUnreadMessagesCount();
                eventBus.goToClientDemandsModule(null, Constants.CLIENT_OFFERED_DEMANDS);
            }
        });
        view.getClientAssignedDemandsButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.closeSubMenu();
                eventBus.updateUnreadMessagesCount();
                eventBus.goToClientDemandsModule(null, Constants.CLIENT_ASSIGNED_DEMANDS);
            }
        });
        view.getClientClosedDemandsButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.closeSubMenu();
                eventBus.updateUnreadMessagesCount();
                eventBus.goToClientDemandsModule(null, Constants.CLIENT_CLOSED_DEMANDS);
            }
        });
        view.getClientRatingsButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.closeSubMenu();
                eventBus.updateUnreadMessagesCount();
                eventBus.goToClientDemandsModule(null, Constants.CLIENT_RATINGS);
            }
        });
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    /**
     * Creates ClientDemands module.
     * @param filter - search criteria
     * @param loadWidget - widget id
     */
    public void onGoToClientDemandsModule(SearchModuleDataHolder filter, int loadWidget) {
        eventBus.loadingDivShow(Storage.MSGS.loading());
        ((ClientToolbarView) view.getToolbarContent()).resetBasic();
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
            case Constants.CLIENT_CLOSED_DEMANDS:
                eventBus.initClientClosedDemands(filter);
                break;
            case Constants.CLIENT_RATINGS:
                eventBus.initClientRatings(filter);
                break;
            default:
                ((ClientToolbarView) view.getToolbarContent()).resetFull();
                eventBus.initClientDemandsWelcome();
                break;
        }
    }

    /**************************************************************************/
    /* Business events handled by presenter */
    /**************************************************************************/
    /**
     * Sets ClientDemands widget.
     * @param content widget
     */
    public void onDisplayView(IsWidget content) {
        view.getContentContainer().setWidget(content);
    }

    /**
     * Displays loading widget.
     * @param loadingMessage
     */
    public void onLoadingDivShow(String loadingMessage) {
        GWT.log("  - loading div created");
        if (loadingDiv == null) {
            loadingDiv = new LoadingDiv();
        }
        view.getContentContainer().clear();
        view.getContentContainer().getElement().appendChild(loadingDiv.getElement());
    }

    /**
     * Hides loading widget.
     */
    public void onLoadingDivHide() {
        GWT.log("  - loading div removed");
        if (view.getContentContainer().getElement().isOrHasChild(loadingDiv.getElement())) {
            view.getContentContainer().getElement().removeChild(loadingDiv.getElement());
        }
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC */
    /**************************************************************************/
    /**************************************************************************/
    /* Client Demands MENU                                                    */
    /**************************************************************************/
    /**
     * Sets active style for clientDemands menu.
     * @param loadedWidget
     */
    public void onClientDemandsMenuStyleChange(int loadedWidget) {
        view.clientMenuStyleChange(loadedWidget);
    }
}