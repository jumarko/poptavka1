package com.eprovement.poptavka.client.home;


import com.eprovement.poptavka.client.root.BaseChildEventBus;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;


@Events(startPresenter = HomePresenter.class, module = HomeModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface HomeEventBus extends EventBus, BaseChildEventBus {

//    @Start
//    @Event(handlers = HomePresenter.class)
//    void start();
//
//    @Forward
//    @Event(handlers = HomePresenter.class)
//    void forward();
//
//    /**************************************************************************/
//    /* Navigation events. */
//    @Event(handlers = HomePresenter.class, historyConverter = HomeHistoryConverter.class)
//    String atHome();
//
//    @Event(forwardToParent = true)
//    void atAccount();
//
//    @Event(forwardToParent = true)
//    void initHomeWelcomeModule(Object object);
//
//    @Event(forwardToModules = HomeSuppliersModule.class)
//    void initHomeSuppliersModule(SearchModuleDataHolder filter, String location);
//
//    @Event(forwardToModules = HomeDemandsModule.class)
//    void initHomeDemandsModule(SearchModuleDataHolder filter, String location);
//
//    @Event(forwardToModules = SupplierCreationModule.class)
//    void goToCreateSupplier(String location);
//
//    @Event(forwardToModules = DemandCreationModule.class)
//    void goToCreateDemand(String location);
//
//    @Event(forwardToModules = SearchModule.class)
//    void initSearchModule(SimplePanel panel);
//
//    /**************************************************************************/
//    @DisplayChildModuleView({HomeSuppliersModule.class,
//        HomeDemandsModule.class, DemandCreationModule.class,
//        SupplierCreationModule.class })
//    @Event(handlers = HomePresenter.class)
//    void setBodyWidget(Widget content);
//
//    /**************************************************************************/
//    /* Parent events. */
//    /*
//     * GENERAL PARENT EVENTS WILL BE LATER SEPARATED WITHIN BASECHILDEVENTBUS TO
//     * SAVE CODE.
//     */
//    /**
//     * Display HomeView - parent Widget for public section.
//     */
//    @Event(forwardToParent = true)
//    void setHomeBodyHolderWidget(Widget body);
//
//    /**
//     * Method for setting public UI layout.
//     */
//    @Event(forwardToParent = true)
//    void setPublicLayout();
//
//    /**
//     * Popup methods for shoving, changing text and hiding, for letting user
//     * know, that application is still working. Every Child Module HAVE TO
//     * implement this method calls. Popup methods for shoving, changing text and
//     * hiding, for letting user know, that application is still working.
//     */
//    @Event(forwardToParent = true)
//    void loadingShow(String loadingMessage);
//
//    @Event(forwardToParent = true)
//    void loadingHide();
//
//    // /* main module calls - common widgets */
//    // @Event(forwardToParent = true)
//    // void initDemandBasicForm(SimplePanel holderWidget);
//    @Event(forwardToParent = true)
//    void initCategoryWidget(SimplePanel holderWidget);
//
//    @Event(forwardToParent = true)
//    void initLocalityWidget(SimplePanel holderWidget);
//
//    // @Event(forwardToParent = true)
//    // void initDemandAdvForm(SimplePanel holderWidget);
//    // @Event(forwardToParent = true)
//    // void initServiceForm(SimplePanel serviceHolder);
//    //
//    // @Event(forwardToParent = true)
//    // void initSupplierForm(SimplePanel supplierInfoHolder);
//    /**
//     * main module calls - Handler calls TODO praso - I don't like this. Rework
//     * it!
//     */
//    // @Event(forwardToParent = true)
//    // void createDemand(FullDemandDetail newDemand, Long clientId);
//    @Event(forwardToParent = true)
//    void getRootCategories();
//
//    // @Event(forwardToParent = true)
//    // void checkFreeEmail(String value);
//    /**************************************************************************/
//    /* Business events. */
//    /* Business events handled by Presenters. */
////    @Event(handlers = HomePresenter.class, historyConverter = HomeHistoryConverter.class)
////    void displayMenu();
//    /**
//     * Assign widget to selected part and automatically removes previous widget.
//     * Optionally can remove widgets from others anchors TODO praso - rename
//     * this method to changeBody()
//     *
//     * @param content
//     */
//    // @Event(handlers = SearchModulePresenter.class)
//    // void setSearchPanelBody(Widget body);
//    @LoadChildModuleError
//    @Event(handlers = HomePresenter.class)
//    void errorOnLoad(Throwable reason);
//
//    @BeforeLoadChildModule
//    @Event(handlers = HomePresenter.class)
//    void beforeLoad();
//
//    @AfterLoadChildModule
//    @Event(handlers = HomePresenter.class)
//    void afterLoad();
//
//    // <<<<<<< .mine
//    //
//    /** demand creation related events. **/
//    // @Event(handlers = FormLoginPresenter.class)
//    // void initLoginForm(SimplePanel holderWidget);
//    // @Event(handlers = DemandCreationPresenter.class)
//    // void toggleLoginRegistration();
//    // TODO praso - this should be moved somewhere else. Bud I don't know where
//    // :)
//    // @Event(handlers = FormUserRegistrationPresenter.class, passive = true)
//    // void checkFreeEmailResponse(Boolean result);
//    // void checkFreeEmailResponse();
//    // @Event(handlers = FormUserRegistrationPresenter.class)
//    // void initRegistrationForm(SimplePanel holderWidget);
//    // logic flow order representing registering client and then creating his
//    // demand
//    // @Event(handlers = HomeHandler.class)
//    // void registerNewClient(UserDetail newClient);
//    // @Event(handlers = DemandCreationPresenter.class)
//    // void prepareNewDemandForNewClient(UserDetail client);
//    // alternative way of loging - verifying
//    // @Event(handlers = HomeHandler.class)
//    // void verifyExistingClient(UserDetail client);
//    // error output
//    // @Event(handlers = DemandCreationPresenter.class)
//    // void loginError();
//    /** Home category display widget and related call. */
//    @Event(handlers = CategoryDisplayPresenter.class)
//    void initCategoryDisplay(SimplePanel holderWidget);
//
//    @Event(handlers = CategoryDisplayPresenter.class)
//    void displayRootCategories(ArrayList<CategoryDetail> list);
//
//    @Event(handlers = CategoryDisplayPresenter.class)
//    void setCategoryDisplayData(ArrayList<CategoryDetail> list);

    /* Business events handled by Handlers. */
}