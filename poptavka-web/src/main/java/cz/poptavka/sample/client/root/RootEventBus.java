package cz.poptavka.sample.client.root;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.InitHistory;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.annotation.module.ChildModule;
import com.mvp4g.client.annotation.module.ChildModules;
import com.mvp4g.client.annotation.module.DisplayChildModuleView;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.home.creation.DemandCreationModule;
import cz.poptavka.sample.client.home.supplier.SupplierCreationModule;
import cz.poptavka.sample.client.homeWelcome.HomeWelcomeModule;
import cz.poptavka.sample.client.homedemands.HomeDemandsModule;
import cz.poptavka.sample.client.homesuppliers.HomeSuppliersModule;
import cz.poptavka.sample.client.main.common.category.CategorySelectorPresenter;
import cz.poptavka.sample.client.main.common.locality.LocalitySelectorPresenter;
import cz.poptavka.sample.client.main.common.search.SearchModule;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.main.login.LoginPopupPresenter;
import cz.poptavka.sample.client.root.footer.FooterPresenter;
import cz.poptavka.sample.client.root.header.HeaderPresenter;
import cz.poptavka.sample.client.root.menu.MenuPresenter;
import cz.poptavka.sample.client.root.searchBar.SearchBarPresenter;
import cz.poptavka.sample.client.user.UserModule;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;

@Events(startView = RootView.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
@ChildModules({
        @ChildModule(moduleClass = HomeWelcomeModule.class, async = false, autoDisplay = true),
        @ChildModule(moduleClass = UserModule.class, async = true, autoDisplay = false),
        @ChildModule(moduleClass = DemandCreationModule.class, async = true, autoDisplay = true),
        @ChildModule(moduleClass = SupplierCreationModule.class, async = true, autoDisplay = true),
        @ChildModule(moduleClass = HomeDemandsModule.class, async = true, autoDisplay = true),
        @ChildModule(moduleClass = HomeSuppliersModule.class, async = true, autoDisplay = true),
        @ChildModule(moduleClass = SearchModule.class, async = false, autoDisplay = true) })
public interface RootEventBus extends EventBus {
    @Start
    @InitHistory
    @Event(handlers = { RootPresenter.class, MenuPresenter.class,
            SearchBarPresenter.class, FooterPresenter.class,
            HeaderPresenter.class })
    void start();

    @Event(handlers = RootPresenter.class)
    void setMenu(IsWidget menu);

    @DisplayChildModuleView(SearchModule.class)
    @Event(handlers = RootPresenter.class)
    void setSearchBar(IsWidget searchBar);

    @Event(handlers = RootPresenter.class)
    void setFooter(IsWidget footer);

    @Event(handlers = RootPresenter.class)
    void setHeader(IsWidget header);

    @DisplayChildModuleView({ HomeWelcomeModule.class,
            DemandCreationModule.class, SupplierCreationModule.class,
            HomeDemandsModule.class, HomeSuppliersModule.class })
            //UserModule.class })
    @Event(handlers = RootPresenter.class)
    void setBodyHolderWidget(IsWidget body);

    @Event(handlers = RootPresenter.class)
    void loadingShow(String loadingMessage);

    @Event(handlers = RootPresenter.class)
    void loadingHide();

    @Event(handlers = HeaderPresenter.class, historyConverter = RootHistoryConverter.class)
    void atHome();

    @Event(handlers = LoginPopupPresenter.class)
    void login();

    @Event(handlers = HeaderPresenter.class)
    void initLoginWindow();

    @Event(modulesToLoad = UserModule.class, handlers = {
            HeaderPresenter.class, RootPresenter.class })
    void atAccount();

    // Afer login
    @Event(handlers = RootHandler.class)
    void createDemand(FullDemandDetail detail, Long clientId);

    @Event(handlers = RootPresenter.class)
    void initDemandAdvForm(SimplePanel holderWidget);

    @Event(handlers = RootPresenter.class)
    void loadingShowWithAnchor(String progressGettingDemandDataring,
            Widget anchor);

    @Event(handlers = HeaderPresenter.class)
    void setUserLayout();

    @Event(handlers = RootPresenter.class, historyConverter = RootHistoryConverter.class)
    void displayMenu();

    @Event(handlers = HeaderPresenter.class)
    void setPublicLayout();

    @Event(modulesToLoad = UserModule.class, passive = true)
    void clearUserOnUnload();

    /** CategorySelection section. **/
    @Event(handlers = RootPresenter.class)
    void initCategoryWidget(SimplePanel embedToWidget);

    @Event(handlers = CategorySelectorPresenter.class)
    void setCategoryListData(int newListPosition, ArrayList<CategoryDetail> list);

    @Event(handlers = RootHandler.class)
    void getRootCategories();

    @Event(handlers = RootHandler.class)
    void getChildListCategories(int newListPosition, String categoryId);

    /** LocalitySelector section. **/
    @Event(handlers = RootPresenter.class)
    void initLocalityWidget(SimplePanel embedToWidget);

    @Event(handlers = LocalitySelectorPresenter.class)
    void setLocalityData(int localityType,
            ArrayList<LocalityDetail> localityList);

    @Event(handlers = RootHandler.class)
    void getChildLocalities(int localityType, String locCode);

    @Event(handlers = RootHandler.class)
    void getRootLocalities();

    /** Demand Creation common method calls. */
    @Event(handlers = RootPresenter.class)
    void initDemandBasicForm(SimplePanel holderWidget);

    /** Menu control section */
    @Event(modulesToLoad = HomeDemandsModule.class)
    void initHomeDemandsModule(SearchModuleDataHolder filter);

    @Event(modulesToLoad = HomeSuppliersModule.class)
    void initHomeSupplierModule(SearchModuleDataHolder filter);

    @Event(modulesToLoad = HomeWelcomeModule.class)
    void initHomeWelcomeModule(SearchModuleDataHolder filter);

    @Event(modulesToLoad = SearchModule.class)
    void initSearchModule(SimplePanel panel);

    @Event(modulesToLoad = SupplierCreationModule.class)
    void goToCreateSupplier();

    @Event(modulesToLoad = DemandCreationModule.class)
    void goToCreateDemand();

}
