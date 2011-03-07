package cz.poptavka.sample.client.home;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.module.ChildModule;
import com.mvp4g.client.annotation.module.ChildModules;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.common.creation.CreationModule;
import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;
import cz.poptavka.sample.client.home.widget.category.CategorySelectorPresenter;
import cz.poptavka.sample.client.home.widget.locality.LocalitySelectorPresenter;
import cz.poptavka.sample.client.home.demands.DemandsModule;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;


@Events(startView = HomeView.class, module = HomeModule.class)
@ChildModules({
        @ChildModule(moduleClass = CreationModule.class, autoDisplay = false, async = true),
        @ChildModule(moduleClass = DemandsModule.class, autoDisplay = false, async = true)
        })
public interface HomeEventBus extends EventBus {

    /**
     * init method
     */
    @Event(handlers = HomePresenter.class)
    void initHome();

    /**
     * Display HomeView - parent Widget for public section
     *
     * @param body
     */
    @Event(forwardToParent = true)
    void setBodyHolderWidget(Widget body);

    /**
     * Assign widget to selected part
     *
     * @param anchor to be connected to
     * @param content
     */
    @Event(handlers = HomePresenter.class)
    void setAnchorWidget(AnchorEnum anchor, Widget content);

    /* locality methods */
    @Event(handlers = LocalitySelectorPresenter.class)
    void displayLocalityList(LocalityType type, List<LocalityDetail> list);

    @Event(handlers = LocalitySelectorPresenter.class)
    void initLocalitySelector(AnchorEnum anchor);
    /* handler methods for locality */
    @Event(handlers = HomeHandler.class)
    void getLocalities(LocalityType type);

    @Event(handlers = HomeHandler.class)
    void getChildLocalities(LocalityType type, String locCode);

    /* categories */
    @Event(handlers = HomeHandler.class)
    void getRootCategories();

    @Event(handlers = CategorySelectorPresenter.class)
    void displayRootCategories(ArrayList<CategoryDetail> list);

    @Event(handlers = CategorySelectorPresenter.class)
    void initCategorySelector(AnchorEnum anchor);

    @Event(modulesToLoad = CreationModule.class)
    void initDemandCreation();

    @Event(modulesToLoad = DemandsModule.class)
    void displayDemands();
}
