/*
 * DemandCreationEventBus servers all events for module DemandCreationModule.
 *
 * Specification:
 * Wireframe: http://www.webgres.cz/axure/ -> WF zatial chyba
 */
package com.eprovement.poptavka.client.home.createDemand;

import com.eprovement.poptavka.client.root.BaseChildEventBus;
import com.eprovement.poptavka.client.root.footer.FooterPresenter;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.mvp4g.client.event.EventBusWithLookup;
import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
@Events(startPresenter = DemandCreationPresenter.class, module = DemandCreationModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface DemandCreationEventBus extends EventBusWithLookup, BaseChildEventBus {

    /**
     * Start event is called only when module is instantiated first time.
     * We can use it for history initialization.
     */
    @Start
    @Event(handlers = DemandCreationPresenter.class, bind = FooterPresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. It there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     * We can use forward event to switch css style for selected menu button.
     */
    @Forward
    @Event(handlers = DemandCreationPresenter.class)
    void forward();

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**
     * The only entry point to this module due to code-spliting feature.
     */
    @Event(handlers = DemandCreationPresenter.class,
    historyConverter = DemandCreationHistoryConverter.class, name = "createDemand")
    String goToCreateDemandModule();

    /**************************************************************************/
    /* Parent events                                                          */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void atAccount();

    @Event(forwardToParent = true)
    void logout(int widgetToLoad);

    @Event(forwardToParent = true, navigationEvent = true)
    void goToClientDemandsModule(SearchModuleDataHolder filterm, int loadWidget);

    @Event(forwardToParent = true)
    void initAddressWidget(SimplePanel embedToWidget);

    @Event(forwardToParent = true)
    void initCategoryWidget(SimplePanel holderWidget, int checkboxes, int displayCountsOfWhat,
        List<CategoryDetail> categoriesToSet);

    @Event(forwardToParent = true)
    void initLocalityWidget(SimplePanel holderWidget, int checkboxes, int displayCountsOfWhat,
        List<LocalityDetail> localitiesToSet);

    @Event(forwardToParent = true)
    void initActivationCodePopup(BusinessUserDetail client, int widgetToLoad);

    @Event(forwardToParent = true)
    void initUserRegistrationForm(SimplePanel holderPanel);

    /**************************************************************************/
    /* Business events handled by DemandCreatoinPresenter.                    */
    /**************************************************************************/
    @Event(handlers = DemandCreationPresenter.class)
    void initDemandBasicForm(SimplePanel holderWidget);

    @Event(handlers = DemandCreationPresenter.class)
    void initDemandAdvForm(SimplePanel holderWidget);

    @Event(handlers = DemandCreationPresenter.class)
    void restoreDefaultFirstTab();

    /**************************************************************************/
    /* Business events handled by Handlers.                                   */
    /**************************************************************************/
    @Event(handlers = DemandCreationHandler.class)
    void registerNewClient(BusinessUserDetail newClient);

    // TODO praso - FullDemandDetail will obviosuly fell inside the left-over fragment
    // if it is used in another module. And I think it is bacause I saw it in left-over
    @Event(handlers = DemandCreationHandler.class)
    void createDemand(FullDemandDetail detail, Long clientId);
}