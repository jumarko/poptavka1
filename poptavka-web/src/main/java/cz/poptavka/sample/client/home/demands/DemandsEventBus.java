package cz.poptavka.sample.client.home.demands;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;
import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;
import cz.poptavka.sample.client.home.demands.flexPager.FlexPagerPresenter;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;

import java.util.ArrayList;
import java.util.Collection;
/**
 *
 * @author Martin Slavkovsky
 *
 */
@Events(startView = DemandsView.class, module = DemandsModule.class)
public interface DemandsEventBus extends EventBus {

    /**
     * Initialize demands presenter.
     */
    @Event(handlers = DemandsPresenter.class)
    void atDemands();

    @Event(forwardToParent = true)
    void setHomeWidget(AnchorEnum anchor, Widget content, boolean clearOthers);

    //Category
    @Event(handlers = DemandsHandler.class)
    void getCategory(long id);

    @Event(handlers = DemandsHandler.class)
    void getCategories();

    //Locality
    @Event(handlers = DemandsHandler.class)
    void getLocality(long id);

    @Event(handlers = DemandsHandler.class)
    void getLocalities();

    //Demands
    @Event(handlers = DemandsHandler.class)
    void getDemands();

    @Event(handlers = DemandsHandler.class)
    void getDemandsByCategories(Category[] categories);

    @Event(handlers = DemandsHandler.class)
    void getDemandsByLocalities(Locality[] localities);

    //Display
    @Event(handlers = DemandsPresenter.class)
    void setCategoryData(ArrayList<CategoryDetail> list);

    @Event(handlers = DemandsPresenter.class)
    void setLocalityData(ArrayList<LocalityDetail> list);

    // **** MY PAGER *********************************************************************
    @Event(handlers = FlexPagerPresenter.class)
    void displayDemands(Collection<Demand> result);

    @Event(handlers = FlexPagerPresenter.class)
    void beginClicked();

    @Event(handlers = FlexPagerPresenter.class)
    void lessClicked();

    @Event(handlers = FlexPagerPresenter.class)
    void endClicked();

    @Event(handlers = FlexPagerPresenter.class)
    void moreClicked();

    @Event(handlers = FlexPagerPresenter.class)
    void clearFlexTable();

    @Event(handlers = FlexPagerPresenter.class)
    void setRows();

    @Event(handlers = FlexPagerPresenter.class)
    void setPagerLabel();

    @Event(handlers = FlexPagerPresenter.class)
    void changePagerSize();

    @Event(handlers = FlexPagerPresenter.class)
    void setDemand(ClickEvent e);

    @Event(handlers = FlexPagerPresenter.class)
    void getResultsCriteria(ResultCriteria resultCriteria);
}
