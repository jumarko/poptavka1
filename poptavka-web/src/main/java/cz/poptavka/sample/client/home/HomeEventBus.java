package cz.poptavka.sample.client.home;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.module.ChildModule;
import com.mvp4g.client.annotation.module.ChildModules;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.common.category.CategorySelectorPresenter.CategoryType;
import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;
import cz.poptavka.sample.client.home.demands.DemandsModule;
import cz.poptavka.sample.client.home.widget.category.CategoryDisplayPresenter;
import cz.poptavka.sample.shared.domain.CategoryDetail;


@Events(startView = HomeView.class, module = HomeModule.class)
@ChildModules({
        @ChildModule(moduleClass = DemandsModule.class, autoDisplay = false, async = true)
        })
public interface HomeEventBus extends EventBus {

    /** init method. **/
    @Event(handlers = HomePresenter.class, historyConverter = HomeHistoryConverter.class)
    String atHome();

    /** init method. **/
    @Event(handlers = HomePresenter.class, historyConverter = HomeHistoryConverter.class)
    void displayMenu();

    /**
     * Display HomeView - parent Widget for public section.
     *
     * @param body
     */
    @Event(forwardToParent = true)
    void setBodyHolderWidget(Widget body);

    /**
     * Assign widget to selected part and automatically removes previous widget. Optionally can remove widgets from
     * others anchors
     *
     * @param anchor to be connected to
     * @param content
     * @param clearOthers if true, removes widgets from other anchors
     */
    @Event(handlers = HomePresenter.class)
    void setHomeWidget(AnchorEnum anchor, Widget content, boolean clearOthers);

    @Event(handlers = CategoryDisplayPresenter.class)
    void displayRootCategories(ArrayList<CategoryDetail> list);

    @Event(handlers = CategoryDisplayPresenter.class)
    void initCategoryDisplay(AnchorEnum anchor);

    @Event(modulesToLoad = DemandsModule.class)
    void start();

    @Event(forwardToParent = true)
    void getRootCategories();

    @Event(handlers = CategoryDisplayPresenter.class)
    void setCategoryDisplayData(CategoryType type, ArrayList<CategoryDetail> list);

    @Event(forwardToParent = true, historyConverter = HomeHistoryConverter.class)
    String atCreateDemand(boolean homeSection);

}
