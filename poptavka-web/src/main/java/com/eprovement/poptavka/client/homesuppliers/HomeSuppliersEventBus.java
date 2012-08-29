/*
 * HomeSuppliersEventBus servers all events for module HomeSuppliersModule.
 *
 * Specification:
 * Wireframe: http://www.webgres.cz/axure/ -> VR Vypis dodavatelov
 */
package com.eprovement.poptavka.client.homesuppliers;

import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid.IEventBusData;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
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

/**
 *
 * @author martin.slavkovsky
 */
@Events(startPresenter = HomeSuppliersPresenter.class, module = HomeSuppliersModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface HomeSuppliersEventBus extends EventBus, IEventBusData {

    /**
     * Start event is called only when module is instantiated first time.
     * We can use it for history initialization.
     */
    @Start
    @Event(handlers = HomeSuppliersPresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. It there is
     * nothing to carry out in this method we should remove forward event to
     * save the number of method invocations.
     */
    @Forward
    @Event(handlers = HomeSuppliersPresenter.class)
    void forward();

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**
     * The only entry point to this module due to code-splitting and exclusive
     * fragment.
     */
    @Event(handlers = HomeSuppliersPresenter.class, historyConverter = HomeSuppliersHistoryConverter.class)
    String goToHomeSuppliersModule(SearchModuleDataHolder searchDataHolder);

    /**************************************************************************/
    /* Parent events                                                          */
    /**************************************************************************/
    // TODO Praso - GENERAL PARENT EVENTS WILL BE LATER SEPARATED WITHIN BASECHILDEVENTBUS TO SAVE CODE
    @Event(forwardToParent = true)
    void loadingShow(String loadingMessage);

    @Event(forwardToParent = true)
    void loadingHide();

    @Event(forwardToParent = true)
    void setUpSearchBar(IsWidget searchView, boolean cat, boolean loc, boolean advBtn);

    @Event(forwardToParent = true)
    void menuStyleChange(int loadedModule);

    @Event(forwardToParent = true)
    void userMenuStyleChange(int loadedModule);

    /**************************************************************************/
    /* Business events handled by Presenters.                                 */
    /**************************************************************************/
    /**************************************************************************/
    /* Business events handled by Presenters - Display events                 */
    /**************************************************************************/
    // SUPPLIERS
    @Event(handlers = HomeSuppliersPresenter.class)
    void displaySuppliers(List<FullSupplierDetail> list);

    /**************************************************************************/
    /* Business events handled by Handlers.                                   */
    /**************************************************************************/
    // SUPPLIERS
    @Override
    @Event(handlers = HomeSuppliersHandler.class)
    void getDataCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition);

    @Override
    @Event(handlers = HomeSuppliersHandler.class)
    void getData(SearchDefinition searchDefinition);
}
