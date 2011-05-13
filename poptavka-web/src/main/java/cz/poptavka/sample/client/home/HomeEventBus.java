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
import cz.poptavka.sample.client.user.problems.MyProblemsModule;
import cz.poptavka.sample.shared.domain.CategoryDetail;


@Events(startView = HomeView.class, module = HomeModule.class)
@ChildModules({
        @ChildModule(moduleClass = DemandsModule.class, autoDisplay = false, async = true),
        @ChildModule(moduleClass = MyProblemsModule.class, autoDisplay = false, async = true)
        })
public interface HomeEventBus extends EventBus {

    /** init method. **/
    @Event(handlers = HomePresenter.class, historyConverter = HomeHistoryConverter.class)
    void displayMenu();

    /** init method. **/
    @Event(handlers = HomePresenter.class, historyConverter = HomeHistoryConverter.class)
    String atHome();

    /**
     * Method for setting public UI layout
     */
    @Event(forwardToParent = true)
    void setPublicLayout();

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

    @Event(modulesToLoad = MyProblemsModule.class)
    void displayProblems();

    @Event(forwardToParent = true)
    void getRootCategories();

    @Event(handlers = CategoryDisplayPresenter.class)
    void setCategoryDisplayData(CategoryType type, ArrayList<CategoryDetail> list);

    /** History/Navigation events. **/
    @Event(forwardToParent = true, historyConverter = HomeHistoryConverter.class)
    String atCreateDemand(boolean homeSection);

    @Event(modulesToLoad = DemandsModule.class, historyConverter = HomeHistoryConverter.class)
    String atDemands();

    @Event(forwardToParent = true, historyConverter = HomeHistoryConverter.class)
    String atRegisterSupplier();

    /**
     * Popup methods for shoving, changing text and hiding, for letting user know, that application is still working
     */
    @Event(forwardToParent = true)
    void displayLoadingPopup(String loadingMessage);

    @Event(forwardToParent = true)
    void changeLoadingMessage(String loadingMessage);

    @Event(forwardToParent = true)
    void hideLoadingPopup();

}
