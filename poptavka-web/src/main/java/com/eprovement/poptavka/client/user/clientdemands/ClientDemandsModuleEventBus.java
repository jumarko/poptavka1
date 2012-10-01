/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.clientdemands;

import com.eprovement.poptavka.client.root.BaseChildEventBus;
import com.eprovement.poptavka.client.user.clientdemands.widgets.ClientAssignedDemandsPresenter;
import com.eprovement.poptavka.client.user.clientdemands.widgets.ClientOffersPresenter;
import com.eprovement.poptavka.client.user.clientdemands.widgets.ClientDemandsPresenter;
import com.eprovement.poptavka.client.user.widget.DetailsWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid.IEventBusData;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandConversationDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;
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
@Events(startPresenter = ClientDemandsModulePresenter.class, module = ClientDemandsModule.class)
public interface ClientDemandsModuleEventBus extends EventBusWithLookup, IEventBusData, BaseChildEventBus {

    /**
     * First event to be handled.
     */
    @Start
    @Event(handlers = ClientDemandsModulePresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. If there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     */
    @Forward
    @Event(handlers = ClientDemandsModulePresenter.class)
    void forward();

    /**************************************************************************/
    /* Parent events + DetailsWrapper related                                 */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void requestDetailWrapperPresenter();

    //Pozor, staci prezenter zavolat raz a uz je aktivny
    @Event(handlers = {ClientDemandsPresenter.class, ClientOffersPresenter.class,
            ClientAssignedDemandsPresenter.class }, passive = true)
    void responseDetailWrapperPresenter(DetailsWrapperPresenter detailSection);

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**
     * The only entry point to this module. This module is not asynchronous.
     *
     * @param filter - defines data holder to be displayed in advanced search bar
     */
    @Event(handlers = ClientDemandsModulePresenter.class, historyConverter = ClientDemandsModuleHistoryConverter.class)
    String goToClientDemandsModule(SearchModuleDataHolder filterm, int loadWidget);

    @Event(handlers = ClientDemandsPresenter.class)
    void initClientDemands(SearchModuleDataHolder filter);

    @Event(handlers = ClientOffersPresenter.class)
    void initClientOffers(SearchModuleDataHolder filter);

    @Event(handlers = ClientAssignedDemandsPresenter.class)
    void initClientAssignedDemands(SearchModuleDataHolder filter);

    /**************************************************************************/
    /* Navigation Parent events */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void goToHomeDemandsModule(SearchModuleDataHolder filter, int homeDemandsViewType);

    @Event(forwardToParent = true)
    void goToHomeSuppliersModule(SearchModuleDataHolder filter, int homeSupplierViewType);

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

    /**************************************************************************/
    /* Business events handled by DemandModulePresenter.                      */
    /**************************************************************************/
    // Forward methods don't need history converter because they have its owns
    @Event(handlers = ClientDemandsModulePresenter.class)
    void displayView(IsWidget content);

    /**************************************************************************/
    /* Business events handled by ListPresenters.                             */
    /**************************************************************************/
    @Event(handlers = ClientDemandsPresenter.class)
    void displayClientDemands(List<ClientDemandDetail> result);

    @Event(handlers = ClientDemandsPresenter.class)
    void displayClientDemandConversations(List<ClientDemandConversationDetail> result);

    @Event(handlers = ClientOffersPresenter.class)
    void displayClientOfferedDemands(List<ClientDemandDetail> result);

    @Event(handlers = ClientOffersPresenter.class)
    void displayClientOfferedDemandOffers(List<FullOfferDetail> result);

    @Event(handlers = ClientAssignedDemandsPresenter.class)
    void displayClientAssignedDemands(List<FullOfferDetail> result);

    /**************************************************************************/
    /* Business events handled by Handlers.                                   */
    /**************************************************************************/
    @Event(handlers = ClientDemandsModuleHandler.class)
    void requestReadStatusUpdate(List<Long> selectedIdList, boolean newStatus);

    @Event(handlers = ClientDemandsModuleHandler.class)
    void requestStarStatusUpdate(List<Long> userMessageIdList, boolean newStatus);

    @Event(handlers = ClientDemandsModuleHandler.class)
    void requestCloseDemand(FullDemandDetail demandDetail);

    @Event(handlers = ClientDemandsModuleHandler.class)
    void requestAcceptOffer(FullOfferDetail fullOfferDetail);

    @Event(handlers = ClientDemandsModuleHandler.class)
    void requestDeclineOffer(OfferDetail offerDetail);

    /**************************************************************************/
    /* Overriden methods of IEventBusData interface. */
    /* Should be called only from UniversalAsyncGrid. */
    /**************************************************************************/
    @Override
    @Event(handlers = ClientDemandsModuleHandler.class)
    void getDataCount(UniversalAsyncGrid grid, SearchDefinition searchDefinition);

    @Override
    @Event(handlers = ClientDemandsModuleHandler.class)
    void getData(SearchDefinition searchDefinition);

    /**************************************************************************/
    /* Button actions - messaging.                                            */
    /**************************************************************************/
    @Event(handlers = ClientDemandsModuleHandler.class)
    void getOfferStatusChange(OfferDetail offerDetail);
}