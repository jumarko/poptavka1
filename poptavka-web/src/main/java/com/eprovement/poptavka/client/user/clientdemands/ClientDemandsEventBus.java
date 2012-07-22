/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.clientdemands;

import com.eprovement.poptavka.client.user.clientdemands.widgets.ClientAssignedProjectsPresenter;
import com.eprovement.poptavka.client.user.clientdemands.widgets.ClientContestsPresenter;
import com.eprovement.poptavka.client.user.clientdemands.widgets.ClientProjectsPresenter;
import com.eprovement.poptavka.client.user.widget.DetailsWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid.IEventBusData;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectContestantDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectConversationDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectDetail;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBus;
import java.util.List;
import java.util.Map;

@Debug(logLevel = Debug.LogLevel.DETAILED)
@Events(startPresenter = ClientDemandsPresenter.class, module = ClientDemandsModule.class)
public interface ClientDemandsEventBus extends EventBus, IEventBusData {

    /**
     * First event to be handled.
     */
    @Start
    @Event(handlers = ClientDemandsPresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. If there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     */
    @Forward
    @Event(handlers = ClientDemandsPresenter.class)
    void forward();

    /**************************************************************************/
    /* Parent events                                                          */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void requestDetailWrapperPresenter();

//    @Event(handlers = {ClientProjectsPresenter.class, ClientContestsPresenter.class,
//    ClientAssignedProjectsPresenter.class}, passive = true)
    @Event(handlers = ClientProjectsPresenter.class)
    void responseDetailWrapperPresenter(DetailsWrapperPresenter detailSection);
    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**
     * The only entry point to this module. This module is not asynchronous.
     *
     * @param filter - defines data holder to be displayed in advanced search bar
     */
    @Event(handlers = ClientDemandsPresenter.class, historyConverter = ClientDemandsHistoryConverter.class)
    String goToClientDemandsModule(SearchModuleDataHolder filterm, int loadWidget);

    @Event(handlers = ClientProjectsPresenter.class)
    void initClientProjects(SearchModuleDataHolder filter);

    @Event(handlers = ClientContestsPresenter.class)
    void initClientContests(SearchModuleDataHolder filter);

    @Event(handlers = ClientAssignedProjectsPresenter.class)
    void initClientAssignedProjects(SearchModuleDataHolder filter);

    /**************************************************************************/
    /* Navigation Parent events */
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
    /* Business events handled by DemandModulePresenter.                      */
    /**************************************************************************/
    // Forward methods don't need history converter because they have its owns
    @Event(handlers = ClientDemandsPresenter.class)
    void displayView(IsWidget content);

    /**************************************************************************/
    /* Business events handled by ListPresenters.                             */
    /**************************************************************************/
    @Event(handlers = ClientProjectsPresenter.class)
    void displayClientProjects(List<ClientProjectDetail> result);

    @Event(handlers = ClientProjectsPresenter.class)
    void displayClientProjectConversations(List<ClientProjectConversationDetail> result);

    @Event(handlers = ClientContestsPresenter.class)
    void displayClientOfferedProjects(List<ClientProjectDetail> result);

    @Event(handlers = ClientContestsPresenter.class)
    void displayClientProjectContestants(List<ClientProjectContestantDetail> result);

    @Event(handlers = ClientAssignedProjectsPresenter.class)
    void displayClientAssignedProjects(List<ClientProjectContestantDetail> result);

    /**************************************************************************/
    /* Business events handled by Handlers. */
    /**************************************************************************/
    @Event(handlers = ClientDemandsHandler.class)
    void requestReadStatusUpdate(List<Long> selectedIdList, boolean newStatus);

    @Event(handlers = ClientDemandsHandler.class)
    void requestStarStatusUpdate(List<Long> userMessageIdList, boolean newStatus);

    /**************************************************************************/
    /* Overriden methods of IEventBusData interface. */
    /* Should be called only from UniversalAsyncGrid. */
    /**************************************************************************/
    @Override
    @Event(handlers = ClientDemandsHandler.class)
    void getDataCount(UniversalAsyncGrid grid, SearchModuleDataHolder detail);

    @Override
    @Event(handlers = ClientDemandsHandler.class)
    void getData(int start, int count, SearchModuleDataHolder detail, Map<String, OrderType> orderColumns);
}