/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.supplierdemands;

import com.eprovement.poptavka.client.root.BaseChildEventBus;
import com.eprovement.poptavka.client.root.footer.FooterPresenter;
import com.eprovement.poptavka.client.user.supplierdemands.widgets.SupplierAssignedDemandsPresenter;
import com.eprovement.poptavka.client.user.supplierdemands.widgets.SupplierDemandsPresenter;
import com.eprovement.poptavka.client.user.supplierdemands.widgets.SupplierDemandsWelcomePresenter;
import com.eprovement.poptavka.client.user.supplierdemands.widgets.SupplierOffersPresenter;
import com.eprovement.poptavka.client.user.supplierdemands.widgets.SupplierRatingsPresenter;
import com.eprovement.poptavka.client.user.widget.DetailsWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid.IEventBusData;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableGrid;
import com.eprovement.poptavka.shared.domain.DemandRatingsDetail;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.offer.SupplierOffersDetail;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierDashboardDetail;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierPotentialDemandDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBusWithLookup;
import java.util.List;

@Debug(logLevel = Debug.LogLevel.DETAILED)
@Events(startPresenter = SupplierDemandsModulePresenter.class, module = SupplierDemandsModule.class)
public interface SupplierDemandsModuleEventBus extends EventBusWithLookup, IEventBusData, BaseChildEventBus {

    /**
     * First event to be handled.
     */
    @Start
    @Event(handlers = SupplierDemandsModulePresenter.class, bind = FooterPresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. If there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     */
    @Forward
    @Event(handlers = SupplierDemandsModulePresenter.class)
    void forward();

    /**************************************************************************/
    /* Parent events + DetailsWrapper related                                 */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void requestDetailWrapperPresenter();

    @Event(handlers = SupplierDemandsModulePresenter.class)
    void responseDetailWrapperPresenter(DetailsWrapperPresenter detailSection);

    /**************************************************************************/
    /* History events                                                         */
    /**************************************************************************/
    @Event(historyConverter = SupplierDemandsModuleHistoryConverter.class, name = "token")
    String createTokenForHistory();

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    //Module navigation
    //--------------------------------------------------------------------------
    /**
     * The only entry point to this module. This module is not asynchronous.
     *
     * @param filter - defines data holder to be displayed in advanced search bar
     */
    @Event(handlers = SupplierDemandsModulePresenter.class, navigationEvent = true)
    void goToSupplierDemandsModule(SearchModuleDataHolder filterm, int loadWidget);

    //Init by default
    //--------------------------------------------------------------------------
    @Event(handlers = SupplierDemandsPresenter.class)
    void initSupplierDemands(SearchModuleDataHolder filter);

    @Event(handlers = SupplierOffersPresenter.class)
    void initSupplierOffers(SearchModuleDataHolder filter);

    @Event(handlers = SupplierAssignedDemandsPresenter.class)
    void initSupplierAssignedDemands(SearchModuleDataHolder filter);

    @Event(handlers = SupplierDemandsWelcomePresenter.class)
    void initSupplierDemandsWelcome();

    /**************************************************************************/
    /* Parent events                                                          */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void atAccount();

    @Event(forwardToParent = true)
    void goToHomeDemandsModule(SearchModuleDataHolder filter);

    @Event(forwardToParent = true)
    void goToHomeSuppliersModule(SearchModuleDataHolder filter);

    @Event(forwardToParent = true)
    void goToCreateDemandModule();

    @Event(forwardToParent = true)
    void goToCreateSupplierModule();

    @Event(forwardToParent = true)
    void setUpdatedUnreadMessagesCount(UnreadMessagesDetail numberOfMessages);

    @Event(forwardToParent = true)
    void initActionBox(SimplePanel holderWidget, UniversalTableGrid grid);

    /**************************************************************************/
    /* Business events handled by SupplierDemandsModulePresenter.             */
    /**************************************************************************/
    @Event(handlers = SupplierDemandsModulePresenter.class)
    void displayView(IsWidget content);

    /**************************************************************************/
    /* Business events handled by SupplierDemandsModulePresenter.             */
    /**************************************************************************/
    @Event(handlers = SupplierDemandsWelcomePresenter.class)
    void loadSupplierDashboardDetail(SupplierDashboardDetail result);

    /**************************************************************************/
    /* Business events handled by SupplierDemandsPresenter.                   */
    /**************************************************************************/
    @Event(handlers = SupplierDemandsPresenter.class)
    void displaySupplierDemands(List<SupplierPotentialDemandDetail> result);

    @Event(handlers = SupplierDemandsPresenter.class)
    void selectSupplierDemand(SupplierPotentialDemandDetail detail);

    /**************************************************************************/
    /* Business events handled by SupplierOffersPresenter.                      */
    /**************************************************************************/
    @Event(handlers = SupplierOffersPresenter.class)
    void displaySupplierOffers(List<SupplierOffersDetail> result);

    @Event(handlers = SupplierOffersPresenter.class)
    void selectSupplierOffer(SupplierOffersDetail detail);

    /**************************************************************************/
    /* Business events handled by SupplierAssignedDemandsPresenter.           */
    /**************************************************************************/
    @Event(handlers = SupplierAssignedDemandsPresenter.class)
    void displaySupplierAssignedDemands(List<SupplierOffersDetail> result);

    @Event(handlers = SupplierAssignedDemandsPresenter.class)
    void selectSupplierAssignedDemand(SupplierOffersDetail detail);

    @Event(handlers = SupplierAssignedDemandsPresenter.class)
    void responseFeedback();
    /**************************************************************************/
    /* Business events handled by SupplierRatingsPresenter.                   */
    /**************************************************************************/
    @Event(handlers = SupplierRatingsPresenter.class)
    void displaySupplierRatings(List<DemandRatingsDetail> result);

    /**************************************************************************/
    /* Business events handled by Handlers.                                   */
    /**************************************************************************/
    //Requesters
    //--------------------------------------------------------------------------
    @Event(handlers = SupplierDemandsModuleHandler.class)
    void requestEditOffer(long id);

    @Event(handlers = SupplierDemandsModuleHandler.class)
    void requestFinishAndRateClient(long demandID, long offerID, Integer rating, String comment);

    //Updaters
    //--------------------------------------------------------------------------
    @Event(handlers = SupplierDemandsModuleHandler.class)
    void updateUnreadMessagesCount();

    @Event(handlers = SupplierDemandsModuleHandler.class)
    void updateOfferStatus(OfferDetail offerDetail);

    //Getters
    //--------------------------------------------------------------------------
    @Event(handlers = SupplierDemandsModuleHandler.class)
    void getSupplierDemand(long demandID);

    @Event(handlers = SupplierDemandsModuleHandler.class)
    void getSupplierOffer(long offerID);

    @Event(handlers = SupplierDemandsModuleHandler.class)
    void getSupplierAssignedDemand(long offerID);

    @Event(handlers = SupplierDemandsModuleHandler.class)
    void getSupplierDashboardDetail();

    /**************************************************************************/
    /* Overriden methods of IEventBusData interface. */
    /* Should be called only from UniversalAsyncGrid. */
    /**************************************************************************/
    @Override
    @Event(handlers = SupplierDemandsModuleHandler.class)
    void getDataCount(UniversalAsyncGrid grid, SearchDefinition searchDefinition);

    @Override
    @Event(handlers = SupplierDemandsModuleHandler.class)
    void getData(SearchDefinition searchDefinition);

    /**************************************************************************/
    /* Client Demands MENU                                                    */
    /**************************************************************************/
    @Event(handlers = SupplierDemandsModulePresenter.class)
    void supplierMenuStyleChange(int loadedWidget);
}