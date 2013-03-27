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

    /**************************************************************************/
    /* General Module events */
    /**************************************************************************/
    public void onStart() {
        // nothing
    }

    public void onForward() {
        //Must be set before any widget start initialize because of autoDisplay feature
        if (!(Storage.getUser() == null && Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL())) {
            eventBus.updateUnreadMessagesCount();
        }
        eventBus.setBody(view.getWidgetView());
        eventBus.setUpSearchBar(null);
        eventBus.userMenuStyleChange(Constants.USER_SUPPLIER_MODULE);
        eventBus.setExtendedFooterStyle();
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
