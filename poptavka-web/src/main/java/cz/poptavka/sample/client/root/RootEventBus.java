package cz.poptavka.sample.client.root;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.InitHistory;
import com.mvp4g.client.annotation.NotFoundHistory;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.annotation.module.ChildModule;
import com.mvp4g.client.annotation.module.ChildModules;
import com.mvp4g.client.annotation.module.DisplayChildModuleView;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.home.creation.DemandCreationModule;
import cz.poptavka.sample.client.home.supplier.SupplierCreationModule;
import cz.poptavka.sample.client.home.widget.category.CategoryDisplayPresenter;
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
import cz.poptavka.sample.client.user.admin.AdminModule;
import cz.poptavka.sample.client.user.demands.DemandModule;
import cz.poptavka.sample.client.user.menu.UserMenuPresenter;
import cz.poptavka.sample.client.user.messages.MessagesModule;
import cz.poptavka.sample.client.user.settings.SettingsModule;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;

@Events(startView = RootView.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
@ChildModules({
    @ChildModule(moduleClass = HomeWelcomeModule.class, async = false, autoDisplay = true),
    @ChildModule(moduleClass = DemandCreationModule.class, async = true, autoDisplay = false),
    @ChildModule(moduleClass = SupplierCreationModule.class, async = true, autoDisplay = false),
    @ChildModule(moduleClass = HomeDemandsModule.class, async = true, autoDisplay = false),
    @ChildModule(moduleClass = HomeSuppliersModule.class, async = true, autoDisplay = false),
    @ChildModule(moduleClass = SearchModule.class, async = false, autoDisplay = true),
    @ChildModule(moduleClass = DemandModule.class, async = true, autoDisplay = false),
    @ChildModule(moduleClass = MessagesModule.class, async = true, autoDisplay = false),
    @ChildModule(moduleClass = SettingsModule.class, async = true, autoDisplay = false),
    @ChildModule(moduleClass = AdminModule.class, async = true, autoDisplay = false) })
public interface RootEventBus extends EventBus {

    @Start
    @InitHistory
    @Event(handlers = {RootPresenter.class, MenuPresenter.class,
            SearchBarPresenter.class, FooterPresenter.class,
            HeaderPresenter.class })
    void start();

    @NotFoundHistory
    @Event(handlers = RootPresenter.class)
    void notFound();

    @Event(handlers = RootHandler.class)
    void getUser();

    @Event(handlers = RootPresenter.class)
    void setUser(UserDetail user);

    @DisplayChildModuleView(SearchModule.class)
    @Event(handlers = RootPresenter.class)
    void setSearchBar(IsWidget searchBar);

    @Event(modulesToLoad = SearchModule.class)
    void clearSearchContent();

    @Event(handlers = RootPresenter.class)
    void setFooter(IsWidget footer);

    @Event(handlers = RootPresenter.class)
    void setHeader(IsWidget header);

    @DisplayChildModuleView({HomeWelcomeModule.class })
    //SupplierCreationModule.class})
    // DemandCreationModule.class, SupplierCreationModule.class })//,
    // HomeDemandsModule.class, HomeSuppliersModule.class })
    // UserModule.class })
    @Event(handlers = RootPresenter.class)
    void setHomeBodyHolderWidget(IsWidget body);

    @Event(modulesToLoad = DemandModule.class)
    void setUserBodyHolderWidget(IsWidget body);

    @Event(handlers = HeaderPresenter.class, historyConverter = RootHistoryConverter.class)
    String atHome();

    @Event(handlers = {HeaderPresenter.class, RootPresenter.class })
    void atAccount();

    @Event(handlers = LoginPopupPresenter.class)
    void login();

    @Event(handlers = HeaderPresenter.class)
    void initLoginWindow();

    @Event(handlers = RootPresenter.class)
    void loadingShow(String loadingMessage);

    @Event(handlers = RootPresenter.class)
    void loadingShowWithAnchor(String progressGettingDemandDataring,
            Widget anchor);

    @Event(handlers = RootPresenter.class)
    void loadingHide();
    // Afer login

    @Event(handlers = RootHandler.class)
    void createDemand(FullDemandDetail detail, Long clientId);

    @Event(handlers = RootPresenter.class)
    void initDemandAdvForm(SimplePanel holderWidget);

    @Event(handlers = HeaderPresenter.class)
    void setPublicLayout();

    @Event(handlers = HeaderPresenter.class)
    void setUserLayout();

//    @Event(modulesToLoad = UserModule.class, passive = true)
//    void clearUserOnUnload();
//    */
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

    /** Menu section */
    @Event(handlers = RootPresenter.class)
    void setMenu(IsWidget menu);

    @Event(handlers = MenuPresenter.class)
    void setHomeMenu();

    @Event(handlers = UserMenuPresenter.class)
    void setUserMenu();

    @Event(handlers = RootPresenter.class)//, historyConverter = RootHistoryConverter.class)
    void displayMenu();

    /** Home category display widget and related call. */
    @Event(handlers = CategoryDisplayPresenter.class)
    void initCategoryDisplay(SimplePanel holderWidget);

    @Event(handlers = CategoryDisplayPresenter.class)
    void displayRootCategories(ArrayList<CategoryDetail> list);

    @Event(handlers = CategoryDisplayPresenter.class)
    void setCategoryDisplayData(ArrayList<CategoryDetail> list);

    /** Menu control section */
    // HOME
    @Event(modulesToLoad = HomeWelcomeModule.class)
    void initHomeWelcomeModule(SearchModuleDataHolder filter);

    @Event(modulesToLoad = HomeDemandsModule.class)
    void initHomeDemandsModule(SearchModuleDataHolder filter, String location);

    @Event(modulesToLoad = HomeSuppliersModule.class)
    void initHomeSuppliersModule(SearchModuleDataHolder filter, String location);

    @Event(modulesToLoad = SupplierCreationModule.class)
    void initCreateSupplierModule(String location);

    @Event(modulesToLoad = DemandCreationModule.class)
    void initCreateDemandModule(String location);

    // USER
//    @Event(modulesToLoad = UserModule.class)
//    void initMessagesTabModuleInbox(SearchModuleDataHolder filter);
//
//    @Event(modulesToLoad = UserModule.class)
//    void initMessagesTabModuleSent(SearchModuleDataHolder filter);
//
//    @Event(modulesToLoad = UserModule.class)
//    void initMessagesTabModuleTrash(SearchModuleDataHolder filter);
    @Event(modulesToLoad = DemandModule.class)
    void initDemandModule(SearchModuleDataHolder filter, String loadWidget);

    /**
     * @param action - inbox, sent, trash, draft, composeNew, composeNewForwarded, composeReply, displayGrid
     * @param filter - provided by search module
     */
    @Event(modulesToLoad = MessagesModule.class)
    void initMessagesModule(SearchModuleDataHolder filter, String loadWidget);

    @Event(modulesToLoad = SettingsModule.class)
    void initSettings();

    @Event(modulesToLoad = AdminModule.class)
    void initAdminModule(SearchModuleDataHolder filter, String loadWidget);

    // BOTH
    @Event(modulesToLoad = SearchModule.class)
    void initSearchModule(SimplePanel panel);
}
