/*
 * HomeDemandsEventBus servers all events for module HomeDemandsModule.
 *
 * Specification:
 * Wireframe: http://www.webgres.cz/axure/ -> VR Vypis Poptaviek
 */
package cz.poptavka.sample.client.homedemands;

import java.util.List;

import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import java.util.Map;

/**
 * @author Martin Slavkovsky
 */
@Events(startView = HomeDemandsView.class, module = HomeDemandsModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface HomeDemandsEventBus extends EventBus {

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
    @Event(handlers = HomeDemandsPresenter.class, historyConverter = HomeDemandsHistoryConverter.class)
    String goToHomeDemandsModule(SearchModuleDataHolder filter);

    /**************************************************************************/
    /* Parent events                                                          */
    /**************************************************************************/
    // TODO Praso - GENERAL PARENT EVENTS WILL BE LATER SEPARATED WITHIN BASECHILDEVENTBUS TO SAVE CODE
    @Event(forwardToParent = true)
    void loadingShow(String loadingMessage);

    @Event(forwardToParent = true)
    void loadingHide();

    @Event(forwardToParent = true)
    void setUpSearchBar(int loadedWidget);

    @Event(forwardToParent = true)
    void clickDemandsMenuStyleChange();

    @Event(forwardToParent = true)
    void clickDemandsUserMenuStyleChange();
    /**************************************************************************/
    /* Business events handled by Presenters.                                 */
    /**************************************************************************/
    @Event(handlers = HomeDemandsPresenter.class)
    void createAsyncDataProvider(final int resultCount);

    @Event(handlers = HomeDemandsPresenter.class)
    void displayDemands(List<FullDemandDetail> result);

    @Event(handlers = HomeDemandsPresenter.class)
    void setDemand(FullDemandDetail demand);

    /**************************************************************************/
    /* Business events handled by Handlers.                                   */
    /**************************************************************************/
    @Event(handlers = HomeDemandsHandler.class)
    void filterDemandsCount(SearchModuleDataHolder detail, Map<String, OrderType> orderColumns);

    @Event(handlers = HomeDemandsHandler.class)
    void filterDemands(int start, int count, SearchModuleDataHolder detail, Map<String, OrderType> orderColumns);

    // TODO Praso - co s tymito komentarmi? Treba ich odstranit. Je tam nieco dolezite?
    /* Business events. */
    /* Business events handled by Presenters. */
//    @Event(handlers = HomeDemandsPresenter.class)
//    void setLocalityData(ArrayList<LocalityDetail> list);
    //Display
//    @Event(handlers = HomeDemandsPresenter.class)
//    void setCategoryData(ArrayList<CategoryDetail> list);
//    @Event(handlers = HomeDemandsPresenter.class)
//    void setResultSource(String resultSource);
//    @Event(handlers = HomeDemandsPresenter.class)
//    void setResultCount(long resultCount);
//    @Event(handlers = HomeDemandsPresenter.class)
//    void filter();

    /* Business events handled by Handlers. */
//    @Event(handlers = HomeDemandsHandler.class)
//    void getAllDemandsCount();
//
//
//    @Event(handlers = HomeDemandsHandler.class)
//    void getSortedDemandsCount(Map<String, OrderType> orderColumns);
//
//    @Event(handlers = HomeDemandsHandler.class)
//    void getSortedDemands(int start, int count, Map<String, OrderType> orderColumns);
//
//    @Event(handlers = HomeDemandsHandler.class)
//    void getDemandsCountCategory(long id);
//
//    @Event(handlers = HomeDemandsHandler.class)
//    void getDemandsCountLocality(String code);
//
//    @Event(handlers = HomeDemandsHandler.class)
//    void getDemandsCountCategoryLocality(long id, String code);
//
//    @Event(handlers = HomeDemandsHandler.class)
//    void getDemands(int fromResult, int toResult);
//    @Event(handlers = HomeDemandsHandler.class)
//    void getDemandsByCategories(int fromResult, int toResult, long id);
//    @Event(handlers = HomeDemandsHandler.class)
//    void getDemandsByLocalities(int fromResult, int toResult, String code);
//    @Event(handlers = HomeDemandsHandler.class)
//    void getDemandsByCategoriesLocalities(int fromResult, int toResult, long id, String code);
//    @Event(handlers = HomeDemandsHandler.class)
//    void getCategories();
//
//    @Event(handlers = HomeDemandsHandler.class)
//    void getLocalities();
}
