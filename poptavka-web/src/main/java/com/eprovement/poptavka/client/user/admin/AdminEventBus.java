/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.admin;

import com.eprovement.poptavka.client.common.BaseChildEventBus;
import com.eprovement.poptavka.client.detail.interfaces.IDetailModule;
import com.eprovement.poptavka.client.root.gateways.CatLocSelectorGateway;
import com.eprovement.poptavka.client.root.gateways.InfoWidgetsGateway;
import com.eprovement.poptavka.client.user.admin.clients.AdminClientsPresenter;
import com.eprovement.poptavka.client.user.admin.interfaces.HandleAdminResizeEvent;
import com.eprovement.poptavka.client.user.admin.demands.AdminNewDemandsPresenter;
import com.eprovement.poptavka.client.user.admin.interfaces.IAdminModule;
import com.eprovement.poptavka.client.user.admin.system.AdminSystemSettingsPresenter;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid.IEventBusData;
import com.eprovement.poptavka.shared.domain.PropertiesDetail;
import com.eprovement.poptavka.shared.domain.adminModule.AdminDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.OriginDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Debug.LogLevel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBusWithLookup;
import java.util.List;
import java.util.Set;

/**
 * Admin section for Want-Something.com
 * <b><i>Note:</i></b>
 * Not all widgets are used. Actice are only <b>NewDemand</b> and <b>ActiveDemand</b> widgets.
 * @author Martin Slavkovsky
 */
@Debug(logLevel = LogLevel.DETAILED)
@Events(startPresenter = AdminPresenter.class, module = AdminModule.class)
public interface AdminEventBus extends EventBusWithLookup, IEventBusData,
    BaseChildEventBus, IDetailModule.Gateway, CatLocSelectorGateway,
    InfoWidgetsGateway {

    /**
     * Start event is called only when module is instantiated first time.
     * We can use it for history initialization.
     */
    @Start
    @Event(handlers = AdminPresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. It there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     * We can use forward event to switch css style for selected menu button.
     */
    @Forward
    @Event(handlers = AdminPresenter.class, navigationEvent = true)
    void forward();

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**
     * The only entry point to this module due to code-spliting feature.
     *
     * @param filter - defines data holder to be displayed in advanced search bar
     * @param loadWidget - prosim doplnit ???
     */
    @Event(handlers = AdminPresenter.class, historyConverter = AdminHistoryConverter.class, navigationEvent = true)
    void goToAdminModule(SearchModuleDataHolder searchDataHolder, IAdminModule.AdminWidget loadWidget);

    @Event(handlers = AdminPresenter.class)
    void setClientMenuActStyle(IAdminModule.AdminWidget widget);

    /**************************************************************************/
    /* Parent events                                                          */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void atAccount();

    /**************************************************************************/
    /* Common event                                                           */
    /**************************************************************************/
    @Event(broadcastTo = HandleAdminResizeEvent.class)
    void resize(int actualWidth);

    /**************************************************************************/
    /* Business Initialization events                                         */
    /**************************************************************************/
    /** Module Initializatin section. **/
    //display widget in content area
    @Event(handlers = AdminPresenter.class)
    void displayView(IsWidget content);

    /** Submodule Initializatin section. **/
    @Event(handlers = AdminNewDemandsPresenter.class)
    void initNewDemands(SearchModuleDataHolder filter);

    @Event(handlers = AdminNewDemandsPresenter.class)
    void initAssignedDemands(SearchModuleDataHolder filter);

    @Event(handlers = AdminNewDemandsPresenter.class)
    void initActiveDemands(SearchModuleDataHolder filter);

    @Event(handlers = AdminClientsPresenter.class)
    void initClients(SearchModuleDataHolder filter);

    @Event(handlers = AdminSystemSettingsPresenter.class)
    void initAdminSystemSettings();

    /**************************************************************************/
    /* Overriden methods of IEventBusData interface.                          */
    /* Should be called only from UniversalAsyncGrid.                         */
    /**************************************************************************/
    @Override
    @Event(handlers = AdminHandler.class)
    void getDataCount(UniversalAsyncGrid grid, SearchDefinition searchDefinition);

    @Override
    @Event(handlers = AdminHandler.class)
    void getData(UniversalAsyncGrid grid, SearchDefinition searchDefinition, int requestId);

    @Event(handlers = AdminHandler.class)
    void requestApproveDemands(UniversalAsyncGrid grid, Set<AdminDemandDetail> demandsToApprove);

    @Event(handlers = AdminNewDemandsPresenter.class)
    void responseApproveDemands();

    /**************************************************************************/
    /* Business events handled by New Admin presenter                         */
    /**************************************************************************/
    /**
     * Request/Response method pair.
     * Create conversation and return its threadRootId
     * @param supplierId
     * @param supplierDetail
     */
    @Event(handlers = AdminHandler.class)
    void requestCreateConversation(long demandId, long userId);

    @Event(handlers = AdminNewDemandsPresenter.class)
    void responseCreateConversation(long threadRootId);

    /**
     * Request/Response method pair.
     * Get conversation if exist.
     */
    @Event(handlers = AdminHandler.class)
    void requestConversation(long threadRootId, long loggedUserId, long counterPartyUserId);

    @Event(handlers = AdminNewDemandsPresenter.class)
    void responseConversation(List<MessageDetail> chatMessages);

    @Event(handlers = AdminHandler.class)
    void requestOrigins();

    @Event(handlers = AdminClientsPresenter.class)
    void responseOrigins(List<OriginDetail> result);

    @Event(handlers = AdminHandler.class)
    void requestChangeEmail(UniversalAsyncGrid table, long userId, String email);

    @Event(handlers = AdminHandler.class)
    void requestChangeOrigin(UniversalAsyncGrid table, long clietnId, long originId);

    @Event(handlers = AdminHandler.class)
    void requestSystemProperties();

    @Event(handlers = AdminSystemSettingsPresenter.class)
    void responseSystemProperties(List<PropertiesDetail> properties);

    @Event(handlers = AdminHandler.class)
    void requestUpdateSystemProperties(PropertiesDetail properties);

    @Event(handlers = AdminHandler.class)
    void requestCalculateDemandCounts();

    @Event(handlers = AdminSystemSettingsPresenter.class)
    void responseCalculateDemandCounts(Boolean result);

    @Event(handlers = AdminHandler.class)
    void requestCalculateSupplierCounts();

    @Event(handlers = AdminSystemSettingsPresenter.class)
    void responseCalculateSupplierCounts(Boolean result);
}
