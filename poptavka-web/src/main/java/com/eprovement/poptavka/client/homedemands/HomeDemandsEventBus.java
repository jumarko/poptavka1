/*
 * HomeDemandsEventBus servers all events for module HomeDemandsModule.
 *
 * Specification:
 * Wireframe: http://www.webgres.cz/axure/ -> VR Vypis Poptaviek
 */
package com.eprovement.poptavka.client.homedemands;

import com.eprovement.poptavka.client.homesuppliers.TreeItem;
import com.eprovement.poptavka.client.root.BaseChildEventBus;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid.IEventBusData;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.event.EventBusWithLookup;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Martin Slavkovsky
 */
@Events(startPresenter = HomeDemandsPresenter.class, module = HomeDemandsModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface HomeDemandsEventBus extends EventBusWithLookup, IEventBusData, BaseChildEventBus {

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
    @Event(handlers = HomeDemandsPresenter.class)
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

    /**************************************************************************/
    /* History events                                                          */
    /**************************************************************************/
    @Event(historyConverter = HomeDemandsHistoryConverter.class, name = "token")
    String createTokenForHistory(SearchModuleDataHolder filterHolder,
            LinkedList<TreeItem> categoryDetail, int page, FullDemandDetail demandDetail);

    /**************************************************************************/
    /* Parent events                                                          */
    /**************************************************************************/
    // TODO Praso - GENERAL PARENT EVENTS WILL BE LATER SEPARATED WITHIN BASECHILDEVENTBUS TO SAVE CODE
    @Event(forwardToParent = true)
    void loadingShow(String loadingMessage);

    @Event(forwardToParent = true)
    void loadingHide();

    @Event(forwardToParent = true)
    void setUpSearchBar(IsWidget searchView);

    @Event(forwardToParent = true)
    void menuStyleChange(int loadedModule);

    @Event(forwardToParent = true)
    void userMenuStyleChange(int loadedModule);

    /**************************************************************************/
    /* Business events handled by Presenter.                                  */
    /**************************************************************************/
    @Event(handlers = HomeDemandsPresenter.class)
    void setModuleByHistory(SearchModuleDataHolder searchDataHolder,
            LinkedList<TreeItem> tree, CategoryDetail categoryDetail, int page, long supplierID);

    @Event(handlers = HomeDemandsPresenter.class)
    void displayDemands(List<FullDemandDetail> result);

    @Event(handlers = HomeDemandsPresenter.class)
    void selectDemand(FullDemandDetail supplierDetail);

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
    void getCategoryAndSetModuleByHistory(SearchModuleDataHolder searchDataHolder,
            LinkedList<TreeItem> tree, long categoryID, int page, long supplierID);
}
