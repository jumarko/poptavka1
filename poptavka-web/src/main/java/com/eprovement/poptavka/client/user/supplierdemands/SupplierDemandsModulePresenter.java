/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.supplierdemands;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.supplierdemands.widgets.SupplierAssignedDemandsPresenter;
import com.eprovement.poptavka.client.user.supplierdemands.widgets.SupplierDemandsPresenter;
import com.eprovement.poptavka.client.user.supplierdemands.widgets.SupplierOffersPresenter;
import com.eprovement.poptavka.client.user.supplierdemands.widgets.SupplierRatingsPresenter;
import com.eprovement.poptavka.client.user.widget.DetailsWrapperPresenter;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.history.NavigationConfirmationInterface;
import com.mvp4g.client.history.NavigationEventCommand;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

@Presenter(view = SupplierDemandsModuleView.class)
public class SupplierDemandsModulePresenter extends LazyPresenter<
        SupplierDemandsModulePresenter.SupplierDemandsLayoutInterface, SupplierDemandsModuleEventBus>
        implements NavigationConfirmationInterface {

    public interface SupplierDemandsLayoutInterface extends LazyView, IsWidget {

        Button getSupplierNewDemandsButton();

        Button getSupplierOffersButton();

        Button getSupplierAssignedDemandsButton();

        Button getSupplierClosedDemandsButton();

        Button getSupplierRatingsButton();

        void setContent(IsWidget contentWidget);

        void supplierMenuStyleChange(int loadedWidget);

        IsWidget getWidgetView();
    }
    private SupplierDemandsPresenter supplierDemands = null;
    private SupplierOffersPresenter supplierOffers = null;
    private SupplierAssignedDemandsPresenter supplierAssigendDemands = null;
    private SupplierRatingsPresenter supplierRatings = null;
    private Timer timer;

    /**************************************************************************/
    /* General Module events */
    /**************************************************************************/
    public void onStart() {
        if (!Storage.isTimerStarted()) {
            this.onStartSupplierNotificationTimer();
            Storage.setTimerStarted(true);
        }
    }

    public void onForward() {
        eventBus.setBody(view.getWidgetView());
        eventBus.setUpSearchBar(null);
        eventBus.userMenuStyleChange(Constants.USER_SUPPLIER_MODULE);
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
        view.getSupplierClosedDemandsButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToSupplierDemandsModule(null, Constants.SUPPLIER_CLOSED_DEMANDS);
            }
        });
        view.getSupplierRatingsButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToSupplierDemandsModule(null, Constants.SUPPLIER_RATINGS);
            }
        });
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    public void onGoToSupplierDemandsModule(SearchModuleDataHolder filter, int loadWidget) {
        switch (loadWidget) {
            case Constants.SUPPLIER_POTENTIAL_DEMANDS:
                if (supplierDemands != null) {
                    eventBus.removeHandler(supplierDemands);
                }
                supplierDemands = eventBus.addHandler(SupplierDemandsPresenter.class);
                supplierDemands.onInitSupplierDemands(filter);
                break;
            case Constants.SUPPLIER_OFFERS:
                if (supplierOffers != null) {
                    eventBus.removeHandler(supplierOffers);
                }
                supplierOffers = eventBus.addHandler(SupplierOffersPresenter.class);
                supplierOffers.onInitSupplierOffers(filter);
                break;
            case Constants.SUPPLIER_ASSIGNED_DEMANDS:
                if (supplierAssigendDemands != null) {
                    eventBus.removeHandler(supplierAssigendDemands);
                }
                supplierAssigendDemands = eventBus.addHandler(SupplierAssignedDemandsPresenter.class);
                supplierAssigendDemands.onInitSupplierAssignedDemands(filter);
                break;
            case Constants.SUPPLIER_CLOSED_DEMANDS:
                if (supplierAssigendDemands != null) {
                    eventBus.removeHandler(supplierAssigendDemands);
                }
                supplierAssigendDemands = eventBus.addHandler(SupplierAssignedDemandsPresenter.class);
                supplierAssigendDemands.onInitSupplierClosedDemands(filter);
                break;
            case Constants.SUPPLIER_RATINGS:
                if (supplierRatings != null) {
                    eventBus.removeHandler(supplierRatings);
                }
                supplierRatings = eventBus.addHandler(SupplierRatingsPresenter.class);
                supplierRatings.onInitSupplierRatings(filter);
                break;
            default:
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

    public void onResponseDetailWrapperPresenter(DetailsWrapperPresenter detailSection) {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.SUPPLIER_POTENTIAL_DEMANDS:
                supplierDemands.onResponseDetailWrapperPresenter(detailSection);
                break;
            case Constants.SUPPLIER_OFFERS:
                supplierOffers.onResponseDetailWrapperPresenter(detailSection);
                break;
            case Constants.SUPPLIER_ASSIGNED_DEMANDS:
                //nothing by default, just let it pass further.
            case Constants.SUPPLIER_CLOSED_DEMANDS:
                supplierAssigendDemands.onResponseDetailWrapperPresenter(detailSection);
                break;
            default:
                break;
        }
    }

    public void onStartSupplierNotificationTimer() {
        this.startSupplierNotificationTimer(Storage.TIMER_PERIOD_MILISECONDS);
    }

    public void onStopSupplierNotificationTimer() {
        this.timer.cancel();
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

    /**************************************************************************/
    /* Helper Methods                                                         */
    /**************************************************************************/

    /**
     * Method starts <code>Timer</code> which executes updateUnreadMessagesCount method every period. This will keep
     * user updated on new messages or notifications.
     *
     * @param periodMilis period in miliseconds
     */
    private void startSupplierNotificationTimer(int periodMilis) {
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
