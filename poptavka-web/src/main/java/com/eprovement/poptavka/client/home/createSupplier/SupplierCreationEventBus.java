/*
 * SupplierCreationEventBus servers all events for module SupplierCreationModule.
 *
 * Specification:
 * Wireframe: http://www.webgres.cz/axure/ -> WF zatial chyba
 */
package com.eprovement.poptavka.client.home.createSupplier;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBus;
import com.eprovement.poptavka.client.home.createSupplier.widget.SupplierInfoPresenter;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;

/**
 *
 * @author ivan.vlcek
 */
@Events(startPresenter = SupplierCreationPresenter.class, module = SupplierCreationModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface SupplierCreationEventBus extends EventBus {

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

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**
     * The only entry point to this module due to code-spliting feature.
     */
    @Event(handlers = SupplierCreationPresenter.class, historyConverter = SupplierCreationHistoryConverter.class)
    String goToCreateSupplierModule();

    /**************************************************************************/
    /* Parent events                                                          */
    /**************************************************************************/
    // TODO praso - odstranit eventy, ktore niesu potrebne a vlozit ich do predka
    @Event(forwardToParent = true)
    void loadingShow(String loadingMessage);

    @Event(forwardToParent = true)
    void loadingHide();

    @Event(forwardToParent = true)
    void initCategoryWidget(SimplePanel holderWidget, int checkboxes);

    @Event(forwardToParent = true)
    void initLocalityWidget(SimplePanel holderWidget);

    @Event(forwardToParent = true)
    void activateLocalityWidgetPresenter();

    @Event(forwardToParent = true)
    void activateAddressWidgetPresenter();

    @Event(forwardToParent = true)
    void initAddressWidget(SimplePanel holderWidget);

    @Event(forwardToParent = true)
    void setUpSearchBar(IsWidget searchView, boolean cat, boolean loc, boolean advBtn);

    @Event(forwardToParent = true)
    void menuStyleChange(int loadedModule);

    @Event(forwardToParent = true)
    void userMenuStyleChange(int loadedModule);
    /**************************************************************************/
    /* Business events handled by Presenters.                                 */
    /**************************************************************************/
    @Event(handlers = SupplierCreationPresenter.class)
    void initServiceForm(SimplePanel serviceHolder);

    @Event(handlers = SupplierCreationPresenter.class)
    void initSupplierForm(SimplePanel supplierInfoHolder);

    @Event(handlers = SupplierInfoPresenter.class)
    void checkFreeEmailResponse(Boolean result);

    /**************************************************************************/
    /* Business events handled by Handlers.                                   */
    /**************************************************************************/
    @Event(handlers = SupplierCreationHandler.class)
    void registerSupplier(BusinessUserDetail newSupplier);

    @Event(handlers = SupplierCreationHandler.class)
    void checkFreeEmail(String value);

}