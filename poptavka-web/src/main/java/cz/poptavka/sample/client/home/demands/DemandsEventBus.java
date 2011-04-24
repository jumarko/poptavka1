package cz.poptavka.sample.client.home.demands;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.event.EventBus;
import com.mvp4g.client.annotation.Events;

import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
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

    /**
     * Display demands on success initialization of demands presenter.
     * @param result - retrieved demands
     */
    @Event(handlers = DemandsPresenter.class)
    void displayDemands();

    @Event(handlers = DemandsPresenter.class)
    void displayDemandsByCategory(Category category);

    @Event(forwardToParent = true)
    void setHomeWidget(AnchorEnum anchor, Widget content, boolean clearOthers);

  //handler methods
    @Event(handlers = DemandsPresenter.class)
    void getLocalities();

    @Event(handlers = DemandsPresenter.class)
    void getCategories();

    @Event(handlers = DemandsPresenter.class)
    void filterByCategory(String code);

    @Event(handlers = DemandsPresenter.class)
    void filterByLocality(String code);

    @Event(handlers = DemandsPresenter.class)
    void setCategoryData(ListBox box, ArrayList<CategoryDetail> list);

    @Event(handlers = DemandsPresenter.class)
    void setLocalityData(ListBox box, ArrayList<LocalityDetail> list);
}
