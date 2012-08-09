/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.supplierdemands;

import com.eprovement.poptavka.client.user.supplierdemands.widgets.SupplierAssignedProjectsPresenter;
import com.eprovement.poptavka.client.user.supplierdemands.widgets.SupplierContestsPresenter;
import com.eprovement.poptavka.client.user.supplierdemands.widgets.SupplierProjectsPresenter;
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
import com.mvp4g.client.event.EventBus;
import java.util.List;

@Debug(logLevel = Debug.LogLevel.DETAILED)
@Events(startPresenter = SupplierDemandsPresenter.class, module = SupplierDemandsModule.class)
public interface SupplierDemandsEventBus extends EventBus, IEventBusData {

    /**
     * First event to be handled.
     */
    @Start
    @Event(handlers = SupplierDemandsPresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. If there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     */
    @Forward
    @Event(handlers = SupplierDemandsPresenter.class)
    void forward();

    /**************************************************************************/
    /* Parent events + DetailsWrapper related                                 */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void requestDetailWrapperPresenter();

    //Pozor, staci prezenter zavolat raz a uz je aktivny
    @Event(handlers = {SupplierProjectsPresenter.class, SupplierContestsPresenter.class,
            SupplierAssignedProjectsPresenter.class }, passive = true)
    void responseDetailWrapperPresenter(DetailsWrapperPresenter detailSection);

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**
     * The only entry point to this module. This module is not asynchronous.
     *
     * @param filter - defines data holder to be displayed in advanced search bar
     */
    @Event(handlers = SupplierDemandsPresenter.class, historyConverter = SupplierDemandsHistoryConverter.class)
    String goToSupplierDemandsModule(SearchModuleDataHolder filterm, int loadWidget);

    @Event(handlers = SupplierProjectsPresenter.class)
    void initSupplierProjects(SearchModuleDataHolder filter);

    @Event(handlers = SupplierContestsPresenter.class)
    void initSupplierContests(SearchModuleDataHolder filter);

    @Event(handlers = SupplierAssignedProjectsPresenter.class)
    void initSupplierAssignedProjects(SearchModuleDataHolder filter);

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

    /**************************************************************************/
    /* Business events handled by Presenters. */
    /**************************************************************************/
    @Event(handlers = SupplierDemandsPresenter.class)
    void displayView(IsWidget content);

    @Event(handlers = {SupplierProjectsPresenter.class, SupplierContestsPresenter.class,
            SupplierAssignedProjectsPresenter.class }, passive = true)
    void displaySupplierDemandsData(List<FullOfferDetail> result);

//    @Event(handlers = SupplierProjectsPresenter.class)
//    void displaySupplierPotentialProjects(List<FullOfferDetail> result);
//
//    @Event(handlers = SupplierContestsPresenter.class)
//    void displaySupplierContests(List<FullOfferDetail> result);
//
//    @Event(handlers = SupplierAssignedProjectsPresenter.class)
//    void displaySupplierAssignedProjects(List<FullOfferDetail> result);
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
    @Event(handlers = SupplierDemandsHandler.class)
    void requestReadStatusUpdate(List<Long> selectedIdList, boolean newStatus);

    @Event(handlers = SupplierDemandsHandler.class)
    void requestStarStatusUpdate(List<Long> userMessageIdList, boolean newStatus);

    /**************************************************************************/
    /* Overriden methods of IEventBusData interface. */
    /* Should be called only from UniversalAsyncGrid. */
    /**************************************************************************/
    @Override
    @Event(handlers = SupplierDemandsHandler.class)
    void getDataCount(UniversalAsyncGrid grid, SearchModuleDataHolder detail);

    @Override
    @Event(handlers = SupplierDemandsHandler.class)
    void getData(SearchDefinition searchDefinition);
}