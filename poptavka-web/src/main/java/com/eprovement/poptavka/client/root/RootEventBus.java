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
package com.eprovement.poptavka.client.root;

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
import com.mvp4g.client.annotation.module.LoadChildModuleError;
import com.mvp4g.client.event.EventBus;

import com.eprovement.poptavka.client.home.createDemand.DemandCreationModule;
import com.eprovement.poptavka.client.home.createSupplier.SupplierCreationModule;
import com.eprovement.poptavka.client.home.widget.category.CategoryDisplayPresenter;
import com.eprovement.poptavka.client.homeWelcome.HomeWelcomeModule;
import com.eprovement.poptavka.client.homedemands.HomeDemandsModule;
import com.eprovement.poptavka.client.homesuppliers.HomeSuppliersModule;
import com.eprovement.poptavka.client.main.common.address.AddressSelectorPresenter;
import com.eprovement.poptavka.client.main.common.category.CategorySelectorPresenter;
import com.eprovement.poptavka.client.main.common.locality.LocalitySelectorPresenter;
import com.eprovement.poptavka.client.main.common.search.SearchModule;
import com.eprovement.poptavka.client.main.common.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.main.login.LoginPopupPresenter;
import com.eprovement.poptavka.client.root.footer.FooterPresenter;
import com.eprovement.poptavka.client.root.header.HeaderPresenter;
import com.eprovement.poptavka.client.root.menu.MenuPresenter;
import com.eprovement.poptavka.client.root.menu.UserMenuPresenter;
import com.eprovement.poptavka.client.root.searchBar.SearchBarPresenter;
import com.eprovement.poptavka.client.user.admin.AdminModule;
import com.eprovement.poptavka.client.user.demands.DemandModule;
import com.eprovement.poptavka.client.user.messages.MessagesModule;
import com.eprovement.poptavka.client.user.settings.SettingsModule;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;

@Events(startPresenter = RootPresenter.class)
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
     * When your application starts, you may want to automatically fire an event
     * so that actions needed at first can occur.
     */
    @Start
    @InitHistory
    @Event(handlers = {HeaderPresenter.class, MenuPresenter.class, SearchBarPresenter.class,
            RootPresenter.class, FooterPresenter.class })
    void start();

    /**************************************************************************/
    /* Layout events.                                                         */
    /**************************************************************************/
    @Event(handlers = RootPresenter.class)
    void setHeader(IsWidget header);

    @Event(handlers = RootPresenter.class)
    void setMenu(IsWidget menu);

    @DisplayChildModuleView(SearchModule.class)
    @Event(handlers = RootPresenter.class)
    void setSearchBar(IsWidget searchBar);

    @Event(handlers = RootPresenter.class)
    void setUpSearchBar(IsWidget searchView, boolean cat, boolean loc, boolean advBtn);

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

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**************************************************************************/
    /* Navigation events - Home menu control section                          */
    /**************************************************************************/
    // TODO praso - nechyba tu nahodou historyConverter?
    // Martin - este neviem, skusal som, ale boli problemy s tym
    @Event(forwardToModules = HomeWelcomeModule.class)//, historyConverter = RootHistoryConverter.class)
    void goToHomeWelcomeModule(SearchModuleDataHolder filter);
//    String goToHomeWelcomeModule(SearchModuleDataHolder filter);

    @Event(forwardToModules = HomeDemandsModule.class)
    void goToHomeDemandsModule(SearchModuleDataHolder filter);

    @Event(forwardToModules = HomeSuppliersModule.class)
    void goToHomeSuppliersModule(SearchModuleDataHolder filter);

    // TODO martin - Preco v tychto metodach nepouzivas filter? Search Bar predsa bude aj v tychto
    // pohladoch. Alebo je tam nejaky default filter?
    @Event(forwardToModules = SupplierCreationModule.class)
    void goToCreateSupplierModule();

    // TODO martin - Preco v tychto metodach nepouzivas filter? Search Bar predsa bude aj v tychto
    // pohladoch. Alebo je tam nejaky default filter?
    @Event(forwardToModules = DemandCreationModule.class)
    void goToCreateDemandModule();

    /**************************************************************************/
    /* Navigation events - User menu control section                          */
    /**************************************************************************/
    @Event(forwardToModules = DemandModule.class) //, historyConverter = RootHistoryConverter.class)
    void goToDemandModule(SearchModuleDataHolder filter, int loadWidget);
//    String goToDemandModule(SearchModuleDataHolder filter, int loadWidget);

    /**
     * @param action - inbox, sent, trash, draft, composeNew, composeNewForwarded, composeReply, displayGrid
     * @param filter - provided by search module
     */
    @Event(forwardToModules = MessagesModule.class)
    void goToMessagesModule(SearchModuleDataHolder filter, int loadWidget);

    @Event(forwardToModules = SettingsModule.class)
    void goToSettingsModule();

    @Event(forwardToModules = AdminModule.class)
    void goToAdminModule(SearchModuleDataHolder filter, int loadWidget);

    /* Both Home and User menut control section */
    @Event(forwardToModules = SearchModule.class)
    void goToSearchModule();

    // TODO Praso - mozeme odstranit? No usage
    @Event(forwardToModules = SearchModule.class)
    void clearSearchContent();

    /**************************************************************************/
    /* Navigation events - Other control sections                             */
    /**************************************************************************/
    /**
     * Logout usera prechadza vzdy cez tuto metodu. Nastavuje sa menu, hlavicka
     * a defaultny modul po prihlaseni.
     *
     * TODO praso - chyba tu zrejme historyConverter
     */
    @Event(handlers = {HeaderPresenter.class, RootPresenter.class, MenuPresenter.class },
    historyConverter = RootHistoryConverter.class)
    String atHome();

    /**
     * Login usera prechadza vzdy cez tuto metodu. Nastavuje sa menu, hlavicka,
     * cookies a defaultny modul po odhlaseni
     *
     * TODO praso - chyba tu zrejme historyConverter
     */
    @Event(handlers = {HeaderPresenter.class, RootPresenter.class, UserMenuPresenter.class },
    historyConverter = RootHistoryConverter.class)
    String atAccount();

    /**
     * This event will be called in case an error occurs while loading the
     * ChildModule code.
     *
     * @param reason - An object may be fired for the event used in case of
     * error but the type of this object must be compatible with
     * java.lang.Throwable. In this case, the error returned by the
     * RunAsync object is passed to the event.
     */
    @LoadChildModuleError
    @Event(handlers = RootPresenter.class)
    void errorOnLoad(Throwable reason);

    /**
     * This event will be called before starting to load the ChildModule code.
     * You can for example decide to display a wait popup.
     *
     * Zatial zakomentovane. Mozno to nebudeme potrebovat kvoli zrychleniu aplikacie
     */
//    @BeforeLoadChildModule
//    @Event(handlers = RootPresenter.class)
//    void beforeLoad();
    /**
     * This event will be called after the code is done loading.
     * You can for example decide to hide a wait popup.
     *
     * Zatial zakomentovane. Mozno to nebudeme potrebovat kvoli zrychleniu aplikacie
     */
//    @AfterLoadChildModule
//    @Event(handlers = RootPresenter.class)
//    void afterLoad();
    /**************************************************************************/
    /* Parent events - no events for RootModule                               */
    /**************************************************************************/
    /**************************************************************************/
    /* Business events handled by Presenters.                                 */
    /**************************************************************************/
    // TODO Praso - mozeme odstranit? No usage
    @NotFoundHistory
    @Event(handlers = RootPresenter.class)
    void notFound();

//    @Event(handlers = RootPresenter.class)
//    void setUser(BusinessUserDetail user);
    // TODO Praso - mozeme odstranit? No usage
    // Martin - ano moze, pouziva ale priamo volane z prezentera
    @Event(handlers = LoginPopupPresenter.class)
    void login();

    // TODO Praso - mozeme odstranit? No usage
    // Martin - ano moze, toto sa nepouziva cez eventBus
    @Event(handlers = HeaderPresenter.class)
    void initLoginWindow();

    @Event(handlers = RootPresenter.class)
    void loadingShow(String loadingMessage);

    // TODO Praso - mozeme odstranit? No usage
    @Event(handlers = RootPresenter.class)
    void loadingShowWithAnchor(String progressGettingDemandDataring,
            Widget anchor);

    // TODO praso - zakomentoval som tieto dve metody na loadovanie cakacieho popupu
    // Chcem pouzit standardnu funkciu cez onBefore, onAfter
    @Event(handlers = RootPresenter.class)
    void loadingHide();
    // Afer login

    // TODO Praso - mozeme odstranit? No usage
    @Event(handlers = RootPresenter.class)
    void initDemandAdvForm(SimplePanel holderWidget);

    // TODO Praso - tuto metodu vola sibling module DemandCreationModule a SupplierCreationModule
    /** CategorySelection section. **/
    @Event(handlers = RootPresenter.class)
    void initCategoryWidget(SimplePanel embedToWidget);

    @Event(handlers = CategorySelectorPresenter.class)
    void setCategoryListData(int newListPosition, ArrayList<CategoryDetail> list);

    // TODO Praso - tuto metodu vola sibling module DemandCreationModule a SupplierCreationModule
    /** LocalitySelector section. **/
    @Event(handlers = RootPresenter.class)
    void initLocalityWidget(SimplePanel embedToWidget);

    //Musi byt takto, lebo ak sa vrati v stack layout paneli, tak dany presenter uz bude neaktivny
    //a pre response na pripadne zmeny uzivatelom nebude fungovat spravny widget
    @Event(activate = LocalitySelectorPresenter.class, deactivate = AddressSelectorPresenter.class)
    void activateLocalityWidgetPresenter();

    @Event(handlers = RootPresenter.class)
    void initAddressWidget(SimplePanel embedToWidget);

    @Event(activate = AddressSelectorPresenter.class, deactivate = LocalitySelectorPresenter.class)
    void activateAddressWidgetPresenter();

    /**
     * Decide which presenter to use according to previous calls of initLocalityWidget and initAddressWidget methods.
     * @param localityType
     * @param localityList
     */
    @Event(handlers = {LocalitySelectorPresenter.class, AddressSelectorPresenter.class })
    void setLocalityData(LocalityType localityType, ArrayList<LocalityDetail> localityList);

    // TODO Praso - mozeme odstranit? No usage
    /** Demand Creation common method calls. */
    @Event(handlers = RootPresenter.class)
    void initDemandBasicForm(SimplePanel holderWidget);

    // TODO Praso - mozeme odstranit? No usage
    /** Home category display widget and related call. */
    @Event(handlers = CategoryDisplayPresenter.class)
    void initCategoryDisplay(SimplePanel holderWidget);

    // TODO Praso - mozeme odstranit? No usage
    @Event(handlers = CategoryDisplayPresenter.class)
    void displayRootCategories(ArrayList<CategoryDetail> list);

    // TODO Praso - mozeme odstranit? No usage
    @Event(handlers = CategoryDisplayPresenter.class)
    void setCategoryDisplayData(ArrayList<CategoryDetail> list);

    /**************************************************************************/
    /* Business events handled by Handlers.                                   */
    /**************************************************************************/
    @Event(handlers = RootHandler.class)
    void getUser(long userId);

    @Event(handlers = RootHandler.class)
    void getRootCategories();

    @Event(handlers = RootHandler.class)
    void getChildListCategories(int newListPosition, String categoryId);

    @Event(handlers = RootHandler.class)
    void getChildLocalities(final LocalityType localityType, String locCode);

    @Event(handlers = RootHandler.class)
    void getLocalities(final LocalityType localityType);

    /**************************************************************************/
    /* Business events handled by MenuPresenter --- HOME MENU                 */
    /**************************************************************************/
    @Event(handlers = MenuPresenter.class)
    void menuStyleChange(int loadedModule);

    /**************************************************************************/
    /* Business events handled by UserMenuPresenter --- USER MENU             */
    /**************************************************************************/
    @Event(handlers = UserMenuPresenter.class)
    void userMenuStyleChange(int loadedModule);
}
