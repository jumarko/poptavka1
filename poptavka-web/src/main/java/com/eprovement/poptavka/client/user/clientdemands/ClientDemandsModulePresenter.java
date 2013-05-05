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
import com.eprovement.poptavka.client.user.clientdemands.widgets.ClientRatingsPresenter;
import com.eprovement.poptavka.client.user.widget.DetailsWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.LoadingDiv;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
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

        Button getClientClosedDemandsButton();

        Button getClientRatingsButton();

        SimplePanel getContentPanel();

        void clientMenuStyleChange(int loadedView);

        IsWidget getWidgetView();
    }
    private ClientDemandsPresenter clientDemands = null;
    private ClientOffersPresenter clientOffers = null;
    private ClientAssignedDemandsPresenter clientAssigendDemands = null;
    private ClientRatingsPresenter clientRatings = null;
    private LoadingDiv loadingDiv = new LoadingDiv();
    private Timer timer;

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        if (!Storage.isTimerStarted()) {
            this.onStartClientNotificationTimer();
            Storage.setTimerStarted(true);
        }
    }

    public void onForward() {
        eventBus.setBody(view.getWidgetView());
        eventBus.setUpSearchBar(null);
        eventBus.userMenuStyleChange(Constants.USER_CLIENT_MODULE);
    }

    @Override
    public void confirm(NavigationEventCommand event) {
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
        view.getClientClosedDemandsButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToClientDemandsModule(null, Constants.CLIENT_CLOSED_DEMANDS);
            }
        });
        view.getClientRatingsButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToClientDemandsModule(null, Constants.CLIENT_RATINGS);
            }
        });
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    public void onGoToClientDemandsModule(SearchModuleDataHolder filter, int loadWidget) {
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
                if (clientOffers == null) {
                    clientOffers = eventBus.addHandler(ClientOffersPresenter.class);
                }
                clientOffers.onInitClientOffers(filter);
                break;
            case Constants.CLIENT_ASSIGNED_DEMANDS:
                if (clientAssigendDemands != null) {
                    eventBus.removeHandler(clientAssigendDemands);
                }
                clientAssigendDemands = eventBus.addHandler(ClientAssignedDemandsPresenter.class);
                clientAssigendDemands.onInitClientAssignedDemands(filter);
                break;
            case Constants.CLIENT_CLOSED_DEMANDS:
                if (clientAssigendDemands != null) {
                    eventBus.removeHandler(clientAssigendDemands);
                }
                clientAssigendDemands = eventBus.addHandler(ClientAssignedDemandsPresenter.class);
                clientAssigendDemands.onInitClientClosedDemands(filter);
                break;
            case Constants.CLIENT_RATINGS:
                if (clientRatings != null) {
                    eventBus.removeHandler(clientRatings);
                }
                clientRatings = eventBus.addHandler(ClientRatingsPresenter.class);
                clientRatings.onInitClientRatings(filter);
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

    public void onResponseDetailWrapperPresenter(DetailsWrapperPresenter detailSection) {
        switch(Storage.getCurrentlyLoadedView()) {
            case Constants.CLIENT_DEMANDS:
                //nothing by default, just let it pass further.
            case Constants.CLIENT_DEMAND_DISCUSSIONS:
                clientDemands.onResponseDetailWrapperPresenter(detailSection);
                break;
            case Constants.CLIENT_OFFERED_DEMANDS:
                //nothing by default, just let it pass further.
            case Constants.CLIENT_OFFERED_DEMAND_OFFERS:
                clientOffers.onResponseDetailWrapperPresenter(detailSection);
                break;
            case Constants.CLIENT_ASSIGNED_DEMANDS:
                //nothing by default, just let it pass further.
            case Constants.CLIENT_CLOSED_DEMANDS:
                clientAssigendDemands.onResponseDetailWrapperPresenter(detailSection);
                break;
            default:
                break;
        }
    }

    public void onStartClientNotificationTimer() {
        this.startClientNotificationTimer(Storage.TIMER_PERIOD_MILISECONDS);
    }

    public void onStopClientNotificationTimer() {
        this.timer.cancel();
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC */
    /**************************************************************************/
    /**************************************************************************/
    /* Client Demands MENU                                                    */
    /**************************************************************************/
    public void onClientDemandsMenuStyleChange(int loadedWidget) {
        view.clientMenuStyleChange(loadedWidget);
    }

    /**************************************************************************/
    /* Helper Methods                                                         */
    /**************************************************************************/

    /**
     * Method starts <code>Timer</code> which executes updateUnreadMessagesCount method every period. This will keep
     * user updated on new messages or notifications.
     *
     * @param periodMilis period in miliseconds
     */
    private void startClientNotificationTimer(int periodMilis) {
        timer = new Timer() {
            @Override
            public void run() {
                eventBus.updateUnreadMessagesCount();
            }
        };
        // Schedule the timer to run every period.
        timer.scheduleRepeating(periodMilis);
    }

}
