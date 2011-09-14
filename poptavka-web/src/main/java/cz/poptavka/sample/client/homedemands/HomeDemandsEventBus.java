/*
 * HomeDemandsEventBus servers all events for module HomeDemandsModule.
 *
 * Specification:
 * Wireframe: http://www.webgres.cz/axure/ -> VR Vypis Poptaviek
 */
package cz.poptavka.sample.client.homedemands;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBus;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO praso - Add history management.
 * @author praso
 */
@Events(startView = HomeDemandsView.class, module = HomeDemandsModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface HomeDemandsEventBus extends EventBus {

    @Start
    @Event(handlers = HomeDemandsPresenter.class)
    void start();

    /** Popup methods for shoving, changing text and hiding,
     * for letting user know, that application is still working.
     * Every Child Module HAVE TO implement this method calls.
     * Popup methods for shoving, changing text and hiding, for letting user know, that application is still working.
     */
    @Event(forwardToParent = true)
    void loadingShow(String loadingMessage);

    @Event(handlers = HomeDemandsHandler.class)
    void getAllDemandsCount();

    @Event(handlers = HomeDemandsHandler.class)
    void getDemandsCountCategory(long id);

    @Event(handlers = HomeDemandsHandler.class)
    void getDemandsCountLocality(String code);

    @Event(handlers = HomeDemandsPresenter.class)
    void setDemand(FullDemandDetail demand);

//    @Event(forwardToParent = true)
//    String atRegisterSupplier();

    //Demand
    @Event(handlers = HomeDemandsHandler.class)
    void getDemands(int fromResult, int toResult);

    @Event(handlers = HomeDemandsHandler.class)
    void getDemandsByCategories(int fromResult, int toResult, long id);

    @Event(handlers = HomeDemandsHandler.class)
    void getDemandsByLocalities(int fromResult, int toResult, String id);

    @Event(forwardToParent = true)
    void loadingHide();

    /** NO METHODS AFTER THIS **/
    @Event(handlers = HomeDemandsHandler.class)
    void getCategories();

    //Locality
    @Event(handlers = HomeDemandsHandler.class)
    void getLocalities();

    /**
     * Assign widget to selected part and automatically removes previous widget. Optionally can remove widgets from
     * others anchors
     *
     * @param content
     */
    @Event(forwardToParent = true)
    void setBodyWidget(Widget content);

    @Event(handlers = HomeDemandsPresenter.class)
    void setLocalityData(ArrayList<LocalityDetail> list);

    //Display
    @Event(handlers = HomeDemandsPresenter.class)
    void setCategoryData(ArrayList<CategoryDetail> list);

    @Event(handlers = HomeDemandsPresenter.class)
    void setResultSource(String resultSource);

    @Event(handlers = HomeDemandsPresenter.class)
    void setResultCount(long resultCount);

    @Event(handlers = HomeDemandsPresenter.class)
    void createAsyncDataProvider();

    @Event(handlers = HomeDemandsPresenter.class)
    void displayDemands(List<FullDemandDetail> result);

    @Event(handlers = HomeDemandsPresenter.class)
    void goToHomeDemands();

}
