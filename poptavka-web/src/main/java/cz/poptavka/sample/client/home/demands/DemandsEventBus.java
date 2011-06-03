package cz.poptavka.sample.client.home.demands;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;
import cz.poptavka.sample.client.home.demands.demand.DemandPresenter;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;

/**
 *
 * @author Martin Slavkovsky
 *
 */
@Events(startView = DemandsView.class, module = DemandsModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface DemandsEventBus extends EventBus {

    /**
     * Initialize demands presenter.
     */
    @Event(handlers = DemandsPresenter.class)
    void atDemands();

    @Event(forwardToParent = true)
    void setBodyWidget(Widget content);

    //Category
    @Event(handlers = DemandsHandler.class)
    void getCategories();

    //Locality
    @Event(handlers = DemandsHandler.class)
    void getLocalities();

    //Demand
    @Event(handlers = DemandsHandler.class)
    void getDemands(int fromResult, int toResult);

    @Event(handlers = DemandsHandler.class)
    void getAllDemandsCount();

    @Event(handlers = DemandsHandler.class)
    void getDemandsByCategories(int fromResult, int toResult, long id);

    @Event(handlers = DemandsHandler.class)
    void getDemandsByLocalities(int fromResult, int toResult, String id);

    //Display
    @Event(handlers = DemandsPresenter.class)
    void setCategoryData(ArrayList<CategoryDetail> list);

    @Event(handlers = DemandsPresenter.class)
    void setLocalityData(ArrayList<LocalityDetail> list);

    @Event(handlers = DemandsPresenter.class)
    void displayDemands(Collection<DemandDetail> result);

    @Event(handlers = DemandsPresenter.class)
    void setDemand(DemandDetail demand);

    @Event(handlers = DemandsPresenter.class)
    void createAsyncDataProvider(final long result);

    //***************** DEMAND **********************
    @Event(handlers = DemandPresenter.class)
    void attachement();

    @Event(handlers = DemandPresenter.class)
    void login();

    @Event(handlers = DemandPresenter.class)
    void register();
}
