/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.clientdemands;

import com.eprovement.poptavka.client.root.BaseChildEventBus;
import com.eprovement.poptavka.client.user.clientdemands.widgets.ClientAssignedDemandsPresenter;
import com.eprovement.poptavka.client.user.clientdemands.widgets.ClientDemandsPresenter;
import com.eprovement.poptavka.client.user.clientdemands.widgets.ClientDemandsWelcomePresenter;
import com.eprovement.poptavka.client.user.clientdemands.widgets.ClientOffersPresenter;
import com.eprovement.poptavka.client.user.clientdemands.widgets.ClientRatingsPresenter;
import com.eprovement.poptavka.client.user.widget.DetailsWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid.IEventBusData;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableGrid;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.DemandRatingsDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandConversationDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.offer.ClientOfferedDemandOffersDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
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
    @Event(handlers = ClientDemandsModulePresenter.class)
    void loadingDivShow(String loadingMessage);

    @Event(handlers = ClientDemandsModulePresenter.class)
    void loadingDivHide();

    @Event(forwardToParent = true)
    void requestDetailWrapperPresenter();

    @Event(handlers = ClientDemandsModulePresenter.class)
    void responseDetailWrapperPresenter(DetailsWrapperPresenter detailSection);

    @Event(forwardToParent = true)
    void initCategoryWidget(SimplePanel embedToWidget, int checkboxes, int displayCountsOfWhat,
        List<CategoryDetail> categoriesToSet);

    @Event(forwardToParent = true)
    void initLocalityWidget(SimplePanel embedToWidget, int checkboxes, int displayCountsOfWhat,
        List<LocalityDetail> localitiesToSet);

    /**************************************************************************/
    /* History events                                                         */
    /**************************************************************************/
    @Event(historyConverter = ClientDemandsModuleHistoryConverter.class, name = "token")
    String createTokenForHistory();

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
    @Event(handlers = ClientDemandsModulePresenter.class, navigationEvent = true)
    void goToClientDemandsModule(SearchModuleDataHolder filterm, int loadWidget);

    //Init by default
    //--------------------------------------------------------------------------
    @Event(handlers = ClientDemandsWelcomePresenter.class)
    void initClientDemandsWelcome();

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
    void atAccount();

    @Event(forwardToParent = true)
    void goToHomeDemandsModule(SearchModuleDataHolder filter);

    @Event(forwardToParent = true)
    void goToHomeSuppliersModule(SearchModuleDataHolder filter);

    @Event(forwardToParent = true)
    void goToCreateDemandModule();

    @Event(forwardToParent = true)
    void goToCreateSupplierModule();

    @Event(forwardToParent = true)
    void setUpdatedUnreadMessagesCount(int numberOfMessages);

    @Event(forwardToParent = true)
    void initActionBox(SimplePanel holderWidget, UniversalTableGrid grid);
    /**************************************************************************/
    /* Business events handled by ClientDemandsModulePresenter.               */
    /**************************************************************************/
    // Forward methods don't need history converter because they have its owns
    @Event(handlers = ClientDemandsModulePresenter.class)
    void displayView(IsWidget content);

    /**************************************************************************/
    /* Business events handled by ClientDemandsPresenter.                     */
    /**************************************************************************/
    @Event(handlers = ClientDemandsPresenter.class)
    void displayClientDemands(List<ClientDemandDetail> result);

    @Event(handlers = ClientDemandsPresenter.class)
    void displayClientDemandConversations(List<ClientDemandConversationDetail> result);

    @Event(handlers = ClientDemandsPresenter.class)
    void selectClientDemand(ClientDemandDetail detail);

    @Event(handlers = ClientDemandsPresenter.class)
    void selectClientDemandConversation(ClientDemandConversationDetail detail);

    @Event(handlers = ClientDemandsPresenter.class)
    void responseConversationNoData();

    /**************************************************************************/
    /* Business events handled by ClientOffersPresenter.                      */
    /**************************************************************************/
    @Event(handlers = ClientOffersPresenter.class)
    void displayClientOfferedDemands(List<ClientDemandDetail> result);

    @Event(handlers = ClientOffersPresenter.class)
    void displayClientOfferedDemandOffers(List<ClientOfferedDemandOffersDetail> result);

    @Event(handlers = ClientOffersPresenter.class)
    void selectClientOfferedDemand(ClientDemandDetail detail);

    @Event(handlers = ClientOffersPresenter.class)
    void selectClientOfferedDemandOffer(ClientOfferedDemandOffersDetail detail);

    /**************************************************************************/
    /* Business events handled by ClientAssignedDemandsPresenter.             */
    /**************************************************************************/
    @Event(handlers = ClientAssignedDemandsPresenter.class)
    void displayClientAssignedDemands(List<ClientOfferedDemandOffersDetail> result);

    @Event(handlers = ClientAssignedDemandsPresenter.class)
    void selectClientAssignedDemand(ClientOfferedDemandOffersDetail detail);

    @Event(handlers = ClientAssignedDemandsPresenter.class)
    void responseFeedback();

    /**************************************************************************/
    /* Business events handled by ClientAssignedDemandsPresenter.             */
    /**************************************************************************/
    @Event(handlers = ClientRatingsPresenter.class)
    void displayClientRatings(List<DemandRatingsDetail> result);

    /**************************************************************************/
    /* Business events handled by Handlers.                                   */
    /**************************************************************************/
    @Event(handlers = ClientDemandsModuleHandler.class)
    void requestAcceptOffer(long offerid);

    @Event(handlers = ClientDemandsModuleHandler.class)
    void updateUnreadMessagesCount();

    @Event(handlers = ClientDemandsModuleHandler.class)
    void getClientDemand(long clientDemandID);

    @Event(handlers = ClientDemandsModuleHandler.class)
    void getClientDemandConversation(long clientDemandConversationID);

    @Event(handlers = ClientDemandsModuleHandler.class)
    void getClientOfferedDemand(long clientDemandID);

    @Event(handlers = ClientDemandsModuleHandler.class)
    void getClientOfferedDemandOffer(long clientOfferedDemandOfferID);

    @Event(handlers = ClientDemandsModuleHandler.class)
    void getClientAssignedDemand(long offerID);

    @Event(handlers = ClientDemandsModuleHandler.class)
    void requestCloseAndRateSupplier(long demandID, long offerID, Integer rating, String comment);

    /**************************************************************************/
    /* Business events for demand's CRUD operations                           */
    /**************************************************************************/
    @Event(handlers = ClientDemandsModuleHandler.class)
    void requestDeleteDemand(long demandId);

    @Event(handlers = ClientDemandsPresenter.class)
    void responseDeleteDemand(boolean result);

    @Event(handlers = ClientDemandsModuleHandler.class)
    void requestUpdateDemand(long demandId, FullDemandDetail updatedDemand);

    @Event(handlers = ClientDemandsPresenter.class)
    void responseUpdateDemand(FullDemandDetail result);

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
    /**************************************************************************/
    /* Client Demands MENU                                                    */
    /**************************************************************************/
    @Event(handlers = ClientDemandsModulePresenter.class)
    void clientDemandsMenuStyleChange(int loadedWidget);
}
