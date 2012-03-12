/*
 * RootEventBus servers all events for foot module. This is the starting
 * EventBus that handled the very first event in the app.
 *
 * Root Module countains all child modules, and all initial presenters like
 * Header, HomeMenu, UserMenu, SearchPanel, HomeBody, UserBody, Footer.
 *
 * Specification:
 * Wireframe: http://www.webgres.cz/axure/
 */
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
import cz.poptavka.sample.client.root.menu.UserMenuPresenter;
import cz.poptavka.sample.client.root.searchBar.SearchBarPresenter;
import cz.poptavka.sample.client.user.admin.AdminModule;
import cz.poptavka.sample.client.user.demands.DemandModule;
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
    @ChildModule(moduleClass = SearchModule.class, async = false, autoDisplay = true),
    @ChildModule(moduleClass = DemandCreationModule.class, async = true, autoDisplay = true),
    @ChildModule(moduleClass = SupplierCreationModule.class, async = true, autoDisplay = true),
    @ChildModule(moduleClass = HomeDemandsModule.class, async = true, autoDisplay = true),
    @ChildModule(moduleClass = HomeSuppliersModule.class, async = true, autoDisplay = true),
    @ChildModule(moduleClass = DemandModule.class, async = true, autoDisplay = true),
    @ChildModule(moduleClass = MessagesModule.class, async = true, autoDisplay = true),
    @ChildModule(moduleClass = SettingsModule.class, async = true, autoDisplay = true),
    @ChildModule(moduleClass = AdminModule.class, async = true, autoDisplay = true) })
public interface RootEventBus extends EventBus {

    /**
     * First event that loads the layout.
     */
    @Start
    @InitHistory
    @Event(handlers = { HeaderPresenter.class, MenuPresenter.class, SearchBarPresenter.class,
            RootPresenter.class, FooterPresenter.class })
    void start();

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    // TODO praso - place all navigation events to this section and separate them
    // into parent class
    @Event(handlers = RootPresenter.class)
    void goToPage1(String string);

    @Event(handlers = RootPresenter.class)
    void goToPage2(String string);

    /* Home menu control section */
    // TODO praso - nechyba tu nahodou historyConverter?
    @Event(modulesToLoad = HomeWelcomeModule.class)
    void goToHomeWelcomeModule(SearchModuleDataHolder filter);

    @Event(modulesToLoad = HomeDemandsModule.class)
    void goToHomeDemandsModule(SearchModuleDataHolder filter, String location);

    @Event(modulesToLoad = HomeSuppliersModule.class)
    void goToHomeSuppliersModule(SearchModuleDataHolder filter, String location);

    // TODO martin - Preco v tychto metodach nepouzivas filter? Search Bar predsa bude aj v tychto
    // pohladoch. Alebo je tam nejaky default filter?
    @Event(modulesToLoad = SupplierCreationModule.class)
    void goToCreateSupplierModule(String location);

    // TODO martin - Preco v tychto metodach nepouzivas filter? Search Bar predsa bude aj v tychto
    // pohladoch. Alebo je tam nejaky default filter?
    @Event(modulesToLoad = DemandCreationModule.class)
    void goToCreateDemandModule(String location);

    /* User menu control section */
    @Event(modulesToLoad = DemandModule.class)
    void goToDemandModule(SearchModuleDataHolder filter, String loadWidget);

    /**
     * @param action - inbox, sent, trash, draft, composeNew, composeNewForwarded, composeReply, displayGrid
     * @param filter - provided by search module
     */
    @Event(modulesToLoad = MessagesModule.class)
    void goToMessagesModule(SearchModuleDataHolder filter, String loadWidget);

    @Event(modulesToLoad = SettingsModule.class)
    void goToSettingsModule();

    @Event(modulesToLoad = AdminModule.class)
    void goToAdminModule(SearchModuleDataHolder filter, String loadWidget);

    /* Both Home and User menut control section */
    @Event(modulesToLoad = SearchModule.class)
    void goToSearchModule(SimplePanel panel);

    @Event(modulesToLoad = SearchModule.class)
    void clearSearchContent();

    // TODO praso - preco pouzivame setUserBodyHolderWidget a este aj goToDemandModule na inicializaciu
    // demand modulu? Mali by sme mat len jednu navigation event ktora bude inicializovat modul
    // To iste pre SearchModule, ktory sa inicializuje vo viacerych roznych eventoch
//    @Event(modulesToLoad = DemandModule.class)
//    void setUserBodyHolderWidget(IsWidget body);
    /**************************************************************************/
    /* Parent events - no events for RootModule                               */
    /**************************************************************************/
    /**************************************************************************/
    /* Business events handled by Presenters.                                 */
    /**************************************************************************/
    @NotFoundHistory
    @Event(handlers = RootPresenter.class)
    void notFound();

    @Event(handlers = RootPresenter.class)
    void setUser(UserDetail user);

    /**************************************************************************/
    /* Layout events. */
    @Event(handlers = RootPresenter.class)
    void setHeader(IsWidget header);

    @Event(handlers = RootPresenter.class)
    void setMenu(IsWidget menu);

    @DisplayChildModuleView(SearchModule.class)
    @Event(handlers = RootPresenter.class)
    void setSearchBar(IsWidget searchBar);

    /**
     * Pouzitie autodisplay funkcie v RootModule ma za nasledok, ze kazdy modul sa
     * automaticky nastavi do RootPresentera cez metodu setBody(), ktora reprezentuje
     * hlavne telo webstranky. Je nutne anotovat tuto metody aby RootModul vedel,
     * ktora metoda ma nahrat pohlad ChildModulu a zobrazit na webstranke
     */
    @DisplayChildModuleView({
        HomeWelcomeModule.class,
        HomeDemandsModule.class,
        HomeSuppliersModule.class,
        SupplierCreationModule.class,
        DemandCreationModule.class,
        DemandModule.class,
        MessagesModule.class,
        SettingsModule.class,
        AdminModule.class })
    @Event(handlers = RootPresenter.class)
    void setBody(IsWidget body);

    @Event(handlers = RootPresenter.class)
    void setFooter(IsWidget footer);

//    @DisplayChildModuleView({ HomeWelcomeModule.class })
    //SupplierCreationModule.class})
    // DemandCreationModule.class, SupplierCreationModule.class })//,
    // HomeDemandsModule.class, HomeSuppliersModule.class })
    // UserModule.class })
    // TODO praso - preco ste toto zakomentovali??? Mozeme to odstarnit uz mame setBody() hotovu
//    @Event(handlers = RootPresenter.class)
//    void setHomeBodyHolderWidget(IsWidget body);

    /**
     * logout usera prechadza vzdy cez tuto metodu. Nastavuje sa menu, hlavicka
     */
    @Event(handlers = { HeaderPresenter.class, RootPresenter.class, MenuPresenter.class },
            historyConverter = RootHistoryConverter.class)
    String atHome();

    /**
     * login usera prechadza vzdy cez tuto metodu. Nastavuje sa menu, hlavicka, cookies
     *
     * TODO praso - chyba tu zrejme historyConverter
     */
    @Event(handlers = { HeaderPresenter.class, RootPresenter.class, UserMenuPresenter.class })
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

    /** LocalitySelector section. **/
    @Event(handlers = RootPresenter.class)
    void initLocalityWidget(SimplePanel embedToWidget);

    @Event(handlers = LocalitySelectorPresenter.class)
    void setLocalityData(int localityType,
            ArrayList<LocalityDetail> localityList);

    /** Demand Creation common method calls. */
    @Event(handlers = RootPresenter.class)
    void initDemandBasicForm(SimplePanel holderWidget);

    /** Menu section. */
    // TODO I moved this events into atAccount and AtHome. They are handled by menu presenters
//    @Event(handlers = MenuPresenter.class)
//    void setHomeMenu();
//    @Event(handlers = UserMenuPresenter.class)
//    void setUserMenu();
    @Event(handlers = RootPresenter.class)//, historyConverter = RootHistoryConverter.class)
    void displayMenu();

    /** Home category display widget and related call. */
    @Event(handlers = CategoryDisplayPresenter.class)
    void initCategoryDisplay(SimplePanel holderWidget);

    @Event(handlers = CategoryDisplayPresenter.class)
    void displayRootCategories(ArrayList<CategoryDetail> list);

    @Event(handlers = CategoryDisplayPresenter.class)
    void setCategoryDisplayData(ArrayList<CategoryDetail> list);

    /**************************************************************************/
    /* Business events handled by Handlers.                                   */
    /**************************************************************************/
    @Event(handlers = RootHandler.class)
    void createDemand(FullDemandDetail detail, Long clientId);

    @Event(handlers = RootHandler.class)
    void getUser();

    @Event(handlers = RootHandler.class)
    void getRootCategories();

    @Event(handlers = RootHandler.class)
    void getChildListCategories(int newListPosition, String categoryId);

    @Event(handlers = RootHandler.class)
    void getChildLocalities(int localityType, String locCode);

    @Event(handlers = RootHandler.class)
    void getRootLocalities();

    // USER
//    @Event(modulesToLoad = UserModule.class)
//    void initMessagesTabModuleInbox(SearchModuleDataHolder filter);
//
//    @Event(modulesToLoad = UserModule.class)
//    void initMessagesTabModuleSent(SearchModuleDataHolder filter);
//
//    @Event(modulesToLoad = UserModule.class)
//    void initMessagesTabModuleTrash(SearchModuleDataHolder filter);
}
