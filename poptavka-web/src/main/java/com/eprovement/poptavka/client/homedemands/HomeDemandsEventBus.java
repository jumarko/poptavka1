/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.homedemands;

import com.eprovement.poptavka.client.root.gateways.DetailModuleGateway;
import com.eprovement.poptavka.client.common.BaseChildEventBus;
import com.eprovement.poptavka.client.root.gateways.CatLocSelectorGateway;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid.IEventBusData;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.event.EventBusWithLookup;
import java.util.List;

/**
 * HomeDemandsEventBus servers all events for module HomeDemandsModule.
 *
 * Specification:
 * Wireframe: http://www.webgres.cz/axure/ -> VR Vypis Poptaviek
 *
 * @author Martin Slavkovsky
 */
@Events(startPresenter = HomeDemandsPresenter.class, module = HomeDemandsModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface HomeDemandsEventBus extends EventBusWithLookup, IEventBusData,
        BaseChildEventBus, DetailModuleGateway, CatLocSelectorGateway {

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    /**
     * Start event is called only when module is instantiated first time.
     * We can use it for history initialization.
     */
    @Start
    @Event(handlers = HomeDemandsPresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. It there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     * We can use forward event to switch css style for selected menu button.
     */
    @Forward
    @Event(handlers = HomeDemandsPresenter.class, navigationEvent = true)
    void forward();

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**
     * The only entry point to this module due to code-spliting feature.
     *
     * @param filter - defines data holder to be displayed in advanced search bar
     */
    @Event(handlers = HomeDemandsPresenter.class, navigationEvent = true)
    void goToHomeDemandsModule(SearchModuleDataHolder filterHolder);

    @Event(handlers = HomeDemandsPresenter.class, navigationEvent = true)
    void goToHomeDemandsModuleFromWelcome(int categoryIdx, ICatLocDetail category);

    @Event(handlers = HomeDemandsPresenter.class)
    void goToHomeDemandsModuleByHistory(SearchModuleDataHolder searchDataHolder,
            ICatLocDetail categoryDetail, int page, long supplierID);

    /**************************************************************************/
    /* History events                                                          */
    /**************************************************************************/
    @Event(historyConverter = HomeDemandsHistoryConverter.class, name = "token")
    String createTokenForHistory(SearchModuleDataHolder filterHolder,
            ICatLocDetail categoryDetail, int page, FullDemandDetail demandDetail);

    /**************************************************************************/
    /* Business events handled by Presenter.                                  */
    /**************************************************************************/
    @Event(handlers = HomeDemandsPresenter.class)
    void displayDemands(List<FullDemandDetail> result);

    @Event(handlers = HomeDemandsPresenter.class)
    void displayDemandDetail(FullDemandDetail supplierDetail);

    /**************************************************************************/
    /* Business events handled by Handler.                                    */
    /**************************************************************************/
    @Override
    @Event(handlers = HomeDemandsHandler.class)
    void getDataCount(UniversalAsyncGrid grid, SearchDefinition searchDefinition);

    @Override
    @Event(handlers = HomeDemandsHandler.class)
    void getData(SearchDefinition searchDefinition);

    @Event(handlers = HomeDemandsHandler.class)
    void getDemand(long demandId);

    @Event(handlers = HomeDemandsHandler.class)
    void setModuleByHistory(SearchModuleDataHolder searchDataHolder,
            String categoryIdStr, String pageStr, String supplierIdStr);
}
