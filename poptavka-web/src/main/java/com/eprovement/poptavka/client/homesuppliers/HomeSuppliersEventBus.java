/*
 * HomeSuppliersEventBus servers all events for module HomeSuppliersModule.
 *
 * Specification:
 * Wireframe: http://www.webgres.cz/axure/ -> VR Vypis dodavatelov
 */
package com.eprovement.poptavka.client.homesuppliers;

import com.eprovement.poptavka.client.root.gateways.DetailModuleGateway;
import com.eprovement.poptavka.client.common.BaseChildEventBus;
import com.eprovement.poptavka.client.root.gateways.CatLocSelectorGateway;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid.IEventBusData;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBusWithLookup;
import java.util.List;

/**
 *
 * @author martin.slavkovsky
 */
@Events(startPresenter = HomeSuppliersPresenter.class, module = HomeSuppliersModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface HomeSuppliersEventBus extends EventBusWithLookup, IEventBusData,
        BaseChildEventBus, DetailModuleGateway, CatLocSelectorGateway {

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
    @Event(handlers = HomeSuppliersPresenter.class, navigationEvent = true)
    void forward();

    /**************************************************************************/

    /* Navigation events.                                                     */
    /**************************************************************************/
    /**
     * The only entry point to this module due to code-splitting and exclusive
     * fragment.
     */
    @Event(handlers = HomeSuppliersPresenter.class, navigationEvent = true)
    void goToHomeSuppliersModule(SearchModuleDataHolder searchDataHolder);

    @Event(handlers = HomeSuppliersPresenter.class)
    void goToHomeSuppliersModuleByHistory(SearchModuleDataHolder searchDataHolder,
            ICatLocDetail categoryDetail, int page, long supplierID);

    /**************************************************************************/
    /* History events                                                          */
    /**************************************************************************/
    @Event(historyConverter = HomeSuppliersHistoryConverter.class, name = "token")
    void createTokenForHistory(SearchModuleDataHolder searchDataHolder,
            ICatLocDetail categoryDetail, int page, FullSupplierDetail supplierDetail);


    /**************************************************************************/
    /* Business events handled by Presenters.                                 */
    /**************************************************************************/
    @Event(handlers = HomeSuppliersPresenter.class)
    void displaySuppliers(List<FullSupplierDetail> list);

    @Event(handlers = HomeSuppliersPresenter.class)
    void displaySupplierDetail(FullSupplierDetail supplierDetail);

    /**************************************************************************/
    /* Business events handled by Handlers.                                   */
    /**************************************************************************/
    @Override
    @Event(handlers = HomeSuppliersHandler.class)
    void getDataCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition);

    @Override
    @Event(handlers = HomeSuppliersHandler.class)
    void getData(SearchDefinition searchDefinition);

    @Event(handlers = HomeSuppliersHandler.class)
    void getSupplier(long supplierID);

    @Event(handlers = HomeSuppliersHandler.class)
    void setModuleByHistory(SearchModuleDataHolder searchDataHolder,
            String categoryIdStr, String pageStr, String supplierIdStr);
}
