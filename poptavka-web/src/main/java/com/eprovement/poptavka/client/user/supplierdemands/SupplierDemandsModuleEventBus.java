/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.supplierdemands;

import com.eprovement.poptavka.client.root.BaseChildEventBus;
import com.eprovement.poptavka.client.user.supplierdemands.widgets.SupplierAssignedDemandsPresenter;
import com.eprovement.poptavka.client.user.supplierdemands.widgets.SupplierDemandsPresenter;
import com.eprovement.poptavka.client.user.supplierdemands.widgets.SupplierDemandsWelcomePresenter;
import com.eprovement.poptavka.client.user.supplierdemands.widgets.SupplierOffersPresenter;
import com.eprovement.poptavka.client.user.widget.DetailsWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid.IEventBusData;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;
import com.eprovement.poptavka.shared.domain.offer.SupplierOffersDetail;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierPotentialDemandDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBusWithLookup;
import java.util.List;

@Debug(logLevel = Debug.LogLevel.DETAILED)
@Events(startPresenter = SupplierDemandsModulePresenter.class, module = SupplierDemandsModule.class)
public interface SupplierDemandsModuleEventBus extends EventBusWithLookup, IEventBusData, BaseChildEventBus {

    /**
     * First event to be handled.
     */
    @Start
    @Event(handlers = SupplierDemandsModulePresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. If there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     */
    @Forward
    @Event(handlers = SupplierDemandsModulePresenter.class)
    void forward();

    @Event(forwardToParent = true)
    void setBody(IsWidget widget);
    /**************************************************************************/
    /* Parent events + DetailsWrapper related                                 */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void requestDetailWrapperPresenter();

    //Pozor, staci prezenter zavolat raz a uz je aktivny
    @Event(handlers = {SupplierDemandsPresenter.class, SupplierOffersPresenter.class,
            SupplierAssignedDemandsPresenter.class }, passive = true)
    void responseDetailWrapperPresenter(DetailsWrapperPresenter detailSection);

    /**************************************************************************/
    /* History events                                                         */
    /**************************************************************************/
    @Event(historyConverter = SupplierDemandsModuleHistoryConverter.class, name = "token")
    String createTokenForHistory(int tablePage, long selectedId);

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    //Module navigation
    //--------------------------------------------------------------------------
    /**
     * The only entry point to this module. This module is not asynchronous.
     *
     * @param filter - defines data holder to be displayed in advanced search bar
     */
    @Event(handlers = SupplierDemandsModulePresenter.class,
                historyConverter = SupplierDemandsModuleHistoryConverter.class, name = "supplierDemands",
                navigationEvent = true)
    String goToSupplierDemandsModule(SearchModuleDataHolder filterm, int loadWidget);

    //Init by default
    //--------------------------------------------------------------------------
    @Event(handlers = SupplierDemandsPresenter.class)
    void initSupplierDemands(SearchModuleDataHolder filter);

    @Event(handlers = SupplierOffersPresenter.class)
    void initSupplierOffers(SearchModuleDataHolder filter);

    @Event(handlers = SupplierAssignedDemandsPresenter.class)
    void initSupplierAssignedDemands(SearchModuleDataHolder filter);

    @Event(handlers = SupplierDemandsWelcomePresenter.class)
    void initSupplierDemandsWelcome();

    //Init by history
    //--------------------------------------------------------------------------
    @Event(handlers = SupplierDemandsPresenter.class)
    void initSupplierDemandsByHistory(int tablePage, long selectedId, SearchModuleDataHolder filter);

    @Event(handlers = SupplierOffersPresenter.class)
    void initSupplierOffersByHistory(int tablePage, long selectedId, SearchModuleDataHolder filter);

    @Event(handlers = SupplierAssignedDemandsPresenter.class)
    void initSupplierAssignedDemandsByHistory(int tablePage, long selectedId, SearchModuleDataHolder filter);

    /**************************************************************************/
    /* Parent events */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void goToHomeDemandsModule(SearchModuleDataHolder filter);

    @Event(forwardToParent = true)
    void goToHomeSuppliersModule(SearchModuleDataHolder filter);

    @Event(forwardToParent = true)
    void goToCreateDemandModule();

    @Event(forwardToParent = true)
    void goToCreateSupplierModule();

    @Event(forwardToParent = true)
    void atAccount();

    @Event(forwardToParent = true)
    void userMenuStyleChange(int loadedModule);

    @Event(forwardToParent = true)
    void setUpSearchBar(IsWidget searchView);

    @Event(forwardToParent = true)
    void setUpdatedUnreadMessagesCount(int numberOfMessages);

    @Event(forwardToParent = true)
    void loginFromSession();

    /**************************************************************************/
    /* Business events handled by SupplierDemandsModulePresenter.             */
    /**************************************************************************/
    @Event(handlers = SupplierDemandsModulePresenter.class)
    void displayView(IsWidget content);

    /**************************************************************************/
    /* Business events handled by SupplierDemandsPresenter.                   */
    /**************************************************************************/
    @Event(handlers = SupplierDemandsPresenter.class)
    void displaySupplierDemands(List<SupplierPotentialDemandDetail> result);

    @Event(handlers = SupplierDemandsPresenter.class)
    void selectSupplierDemand(SupplierPotentialDemandDetail detail);

    /**************************************************************************/
    /* Business events handled by SupplierOffersPresenter.                      */
    /**************************************************************************/
    @Event(handlers = SupplierOffersPresenter.class)
    void displaySupplierOffers(List<SupplierOffersDetail> result);

    @Event(handlers = SupplierOffersPresenter.class)
    void selectSupplierOffer(SupplierOffersDetail detail);

    /**************************************************************************/
    /* Business events handled by SupplierAssignedDemandsPresenter.           */
    /**************************************************************************/
    @Event(handlers = SupplierAssignedDemandsPresenter.class)
    void displaySupplierAssignedDemands(List<FullOfferDetail> result);

    @Event(handlers = SupplierAssignedDemandsPresenter.class)
    void selectSupplierAssignedDemand(FullOfferDetail detail);

    /**************************************************************************/
    /* Business events handled by Handlers.                                   */
    /**************************************************************************/
    //Requesters
    //--------------------------------------------------------------------------
    @Event(handlers = SupplierDemandsModuleHandler.class)
    void requestReadStatusUpdate(List<Long> selectedIdList, boolean newStatus);

    @Event(handlers = SupplierDemandsModuleHandler.class)
    void requestStarStatusUpdate(List<Long> userMessageIdList, boolean newStatus);

    @Event(handlers = SupplierDemandsModuleHandler.class)
    void requestFinishOffer(long id);

    @Event(handlers = SupplierDemandsModuleHandler.class)
    void requestEditOffer(long id);

    @Event(handlers = SupplierDemandsModuleHandler.class)
    void requestCancelOffer(long id);

    //Updaters
    //--------------------------------------------------------------------------
    @Event(handlers = SupplierDemandsModuleHandler.class)
    void updateUnreadMessagesCount();

    @Event(handlers = SupplierDemandsModuleHandler.class)
    void updateOfferStatus(OfferDetail offerDetail);

    //Getters
    //--------------------------------------------------------------------------
    @Event(handlers = SupplierDemandsModuleHandler.class)
    void getSupplierDemand(long demandID);

    @Event(handlers = SupplierDemandsModuleHandler.class)
    void getSupplierOffer(long offerID);

    @Event(handlers = SupplierDemandsModuleHandler.class)
    void getSupplierAssignedDemand(long offerID);

    /**************************************************************************/
    /* Overriden methods of IEventBusData interface. */
    /* Should be called only from UniversalAsyncGrid. */
    /**************************************************************************/
    @Override
    @Event(handlers = SupplierDemandsModuleHandler.class)
    void getDataCount(UniversalAsyncGrid grid, SearchDefinition searchDefinition);

    @Override
    @Event(handlers = SupplierDemandsModuleHandler.class)
    void getData(SearchDefinition searchDefinition);

    /**************************************************************************/
    /* Client Demands MENU                                                    */
    /**************************************************************************/
    @Event(handlers = SupplierDemandsModulePresenter.class)
    void selectSupplierDemandsMenu(int loadedWidget);
}