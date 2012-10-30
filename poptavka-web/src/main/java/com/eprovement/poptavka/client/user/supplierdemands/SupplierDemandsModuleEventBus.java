/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.supplierdemands;

import com.eprovement.poptavka.client.root.BaseChildEventBus;
import com.eprovement.poptavka.client.user.supplierdemands.widgets.SupplierAssignedDemandsPresenter;
import com.eprovement.poptavka.client.user.supplierdemands.widgets.SupplierOffersPresenter;
import com.eprovement.poptavka.client.user.supplierdemands.widgets.SupplierDemandsPresenter;
import com.eprovement.poptavka.client.user.widget.DetailsWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid.IEventBusData;
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
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**
     * The only entry point to this module. This module is not asynchronous.
     *
     * @param filter - defines data holder to be displayed in advanced search bar
     */
    @Event(handlers = SupplierDemandsModulePresenter.class,
    historyConverter = SupplierDemandsModuleHistoryConverter.class)
    String goToSupplierDemandsModule(SearchModuleDataHolder filterm, int loadWidget);

    @Event(handlers = SupplierDemandsPresenter.class)
    void initSupplierDemands(SearchModuleDataHolder filter);

    @Event(handlers = SupplierOffersPresenter.class)
    void initSupplierOffers(SearchModuleDataHolder filter);

    @Event(handlers = SupplierAssignedDemandsPresenter.class)
    void initSupplierAssignedDemands(SearchModuleDataHolder filter);

    /**************************************************************************/
    /* Parent events */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void goToHomeDemandsModule(SearchModuleDataHolder filter, int homeDemandsViewType);

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
    void populateStorageByUserDetail();

    /**************************************************************************/
    /* Business events handled by Presenters. */
    /**************************************************************************/
    @Event(handlers = SupplierDemandsModulePresenter.class)
    void displayView(IsWidget content);

    @Event(handlers = {SupplierDemandsPresenter.class, SupplierOffersPresenter.class,
            SupplierAssignedDemandsPresenter.class }, passive = true)
    void displaySupplierDemandsData(List<FullOfferDetail> result);

//    @Event(handlers = SupplierDemandsPresenter.class)
//    void displaySupplierPotentialDemands(List<FullOfferDetail> result);
//
//    @Event(handlers = SupplierOffersPresenter.class)
//    void displaySupplierOffers(List<FullOfferDetail> result);
//
//    @Event(handlers = SupplierAssignedDemandsPresenter.class)
//    void displaySupplierAssignedDemands(List<FullOfferDetail> result);
    /**************************************************************************/
    /* Business events handled by DevelDetailWrapperPresenter.                */
    /**************************************************************************/
    /*
     * Request/Response Method pair
     * DemandDetail for detail section
     * @param demandId
     * @param type
     */
//    @Event(handlers = ClientDemandsHandler.class)
//    void requestDemandDetail(Long demandId, ViewType type);
//
//    @Event(handlers = DevelDetailWrapperPresenter.class, passive = true)
//    void responseDemandDetail(FullDemandDetail demandDetail, ViewType type);
//
//    @Event(handlers = ClientDemandsHandler.class)
//    void requestSupplierDetail(Long supplierId, ViewType type);
//
//    @Event(handlers = DevelDetailWrapperPresenter.class, passive = true)
//    void responseSupplierDetail(FullSupplierDetail supplierDetail, ViewType type);

    /*
     * Request/Response method pair
     * Fetch and display chat(conversation) for supplier new demands list
     * @param messageId
     * @param userMessageId
     * @param userId
     */
//    @Event(handlers = ClientDemandsHandler.class)
//    void requestConversation(long messageId, Long userMessageId, Long userId);
//
//    @Event(handlers = DevelDetailWrapperPresenter.class)
//    void responseConversation(ArrayList<MessageDetail> chatMessages, ViewType supplierListType);
    /**************************************************************************/
    /* Business events handled by Handlers. */
    /**************************************************************************/
    @Event(handlers = SupplierDemandsModuleHandler.class)
    void requestReadStatusUpdate(List<Long> selectedIdList, boolean newStatus);

    @Event(handlers = SupplierDemandsModuleHandler.class)
    void requestStarStatusUpdate(List<Long> userMessageIdList, boolean newStatus);

    @Event(handlers = SupplierDemandsModuleHandler.class)
    void requestFinishOffer(FullOfferDetail fullOfferDetail);

    /**
     * Method will get logged user from Spring Authentication object if any and retrieves the number of unread messages
     * for logged user. If no user is logged in the Spring Security configuration will require authentication via
     * loginPopupPresenter in order to carry out RPC service that is intended for authenticated users
     * only.
     */
    @Event(handlers = SupplierDemandsModuleHandler.class)
    void updateUnreadMessagesCount();

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
}