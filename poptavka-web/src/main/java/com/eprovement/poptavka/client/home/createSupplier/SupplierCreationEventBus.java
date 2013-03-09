/*
 * SupplierCreationEventBus servers all events for module SupplierCreationModule.
 *
 * Specification:
 * Wireframe: http://www.webgres.cz/axure/ -> WF zatial chyba
 */
package com.eprovement.poptavka.client.home.createSupplier;

import com.eprovement.poptavka.client.root.BaseChildEventBus;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBusWithLookup;
import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
@Events(startPresenter = SupplierCreationPresenter.class, module = SupplierCreationModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface SupplierCreationEventBus extends EventBusWithLookup, BaseChildEventBus {

    /**
     * Start event is called only when module is instantiated first time.
     * We can use it for history initialization.
     */
    @Start
    @Event(handlers = SupplierCreationPresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. It there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     * We can use forward event to switch css style for selected menu button.
     */
    @Forward
    @Event(handlers = SupplierCreationPresenter.class)
    void forward();

    @Event(forwardToParent = true)
    void setBody(IsWidget widget);
    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**
     * The only entry point to this module due to code-spliting feature.
     */
    @Event(handlers = SupplierCreationPresenter.class, navigationEvent = true)
    void goToCreateSupplierModule();

    @Event(handlers = SupplierCreationPresenter.class, navigationEvent = true)
    void goToCreateSupplierModuleByHistory(int selectedTab);

    /**************************************************************************/
    /* History events                                                         */
    /**************************************************************************/
    @Event(historyConverter = SupplierCreationHistoryConverter.class, name = "createSupplier")
    String registerTabToken(int tab);

    /**************************************************************************/
    /* Parent events                                                          */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void logout(int widgetToLoad);

    // TODO praso - odstranit eventy, ktore niesu potrebne a vlozit ich do predka
    @Event(forwardToParent = true)
    void loadingShow(String loadingMessage);

    @Event(forwardToParent = true)
    void loadingHide();

    @Event(forwardToParent = true)
    void initCategoryWidget(SimplePanel holderWidget, int checkboxes, int displayCountsOfWhat,
        List<CategoryDetail> categoriesToSet);

    @Event(forwardToParent = true)
    void initLocalityWidget(SimplePanel holderWidget, int checkboxes, int displayCountsOfWhat,
        List<LocalityDetail> localitiesToSet);

    @Event(forwardToParent = true)
    void initActivationCodePopup(BusinessUserDetail client, int widgetToLoad);

    @Event(forwardToParent = true)
    void initAddressWidget(SimplePanel holderWidget);

    @Event(forwardToParent = true)
    void initServicesWidget(SimplePanel embedToWidget);

    @Event(forwardToParent = true)
    void setUpSearchBar(IsWidget searchView);

    @Event(forwardToParent = true)
    void menuStyleChange(int loadedModule);

    @Event(forwardToParent = true)
    void userMenuStyleChange(int loadedModule);

    @Event(forwardToParent = true)
    void initUserRegistrationForm(SimplePanel holderPanel);

    /**************************************************************************/
    /* Business events handled by Handlers.                                   */
    /**************************************************************************/
    @Event(handlers = SupplierCreationHandler.class)
    void registerSupplier(FullSupplierDetail newSupplier);

    /**************************************************************************/
    /* Business events handled by Presenter.                                  */
    /**************************************************************************/
    @Event(handlers = SupplierCreationPresenter.class)
    void notifyAddressWidgetListeners();
}