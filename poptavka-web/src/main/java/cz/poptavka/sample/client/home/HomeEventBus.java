package cz.poptavka.sample.client.home;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.annotation.module.AfterLoadChildModule;
import com.mvp4g.client.annotation.module.BeforeLoadChildModule;
import com.mvp4g.client.annotation.module.ChildModule;
import com.mvp4g.client.annotation.module.ChildModules;
import com.mvp4g.client.annotation.module.DisplayChildModuleView;
import com.mvp4g.client.annotation.module.LoadChildModuleError;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.home.creation.DemandCreationModule;
import cz.poptavka.sample.client.home.supplier.SupplierCreationModule;
import cz.poptavka.sample.client.home.widget.category.CategoryDisplayPresenter;
import cz.poptavka.sample.client.homedemands.HomeDemandsModule;
import cz.poptavka.sample.client.homesuppliers.HomeSuppliersModule;
import cz.poptavka.sample.client.main.common.search.SearchDataHolder;
import cz.poptavka.sample.client.main.common.search.SearchHandler;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;

@Events(startView = HomeView.class, module = HomeModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
@ChildModules({
    @ChildModule(moduleClass = HomeDemandsModule.class, async = true, autoDisplay = true),
    @ChildModule(moduleClass = HomeSuppliersModule.class, async = true, autoDisplay = true),
    @ChildModule(moduleClass = SupplierCreationModule.class, async = true, autoDisplay = true),
    @ChildModule(moduleClass = DemandCreationModule.class, async = true, autoDisplay = true)
})
public interface HomeEventBus extends EventBus {

    @Start
    @Event(handlers = HomePresenter.class)
    void start();

    @Forward
    @Event(handlers = HomePresenter.class)
    void forward();

    /**************************************************************************/
    /* Navigation events. */
    @Event(handlers = HomePresenter.class, historyConverter = HomeHistoryConverter.class)
    String atHome();

    @Event(modulesToLoad = HomeSuppliersModule.class)
    void goToHomeSuppliers(SearchDataHolder filter);

    @Event(modulesToLoad = HomeDemandsModule.class)
    void goToHomeDemands(SearchDataHolder filter);

    @Event(modulesToLoad = SupplierCreationModule.class)
    void goToCreateSupplier();

    @Event(modulesToLoad = DemandCreationModule.class)
    void goToCreateDemand();

    /**************************************************************************/
    /* Parent events. */
    /* GENERAL PARENT EVENTS WILL BE LATER SEPARATED WITHIN BASECHILDEVENTBUS TO SAVE CODE. */
    /**
     * Display HomeView - parent Widget for public section.
     */
    @Event(forwardToParent = true)
    void setBodyHolderWidget(Widget body);

    /**
     * Method for setting public UI layout.
     */
    @Event(forwardToParent = true)
    void setPublicLayout();

    /**
     * Popup methods for shoving, changing text and hiding,
     * for letting user know, that application is still working.
     * Every Child Module HAVE TO implement this method calls.
     * Popup methods for shoving, changing text and hiding, for letting user know, that application is still working.
     */
    @Event(forwardToParent = true)
    void loadingShow(String loadingMessage);

    @Event(forwardToParent = true)
    void loadingHide();

//    /* main module calls - common widgets */
//    @Event(forwardToParent = true)
//    void initDemandBasicForm(SimplePanel holderWidget);
    @Event(forwardToParent = true)
    void initCategoryWidget(SimplePanel holderWidget);

    @Event(forwardToParent = true)
    void initLocalityWidget(SimplePanel holderWidget);

//    @Event(forwardToParent = true)
//    void initDemandAdvForm(SimplePanel holderWidget);
//    @Event(forwardToParent = true)
//    void initServiceForm(SimplePanel serviceHolder);
//
//    @Event(forwardToParent = true)
//    void initSupplierForm(SimplePanel supplierInfoHolder);
    /** main module calls - Handler calls
     * TODO praso - I don't like this. Rework it!
     */
//    @Event(forwardToParent = true)
//    void createDemand(FullDemandDetail newDemand, Long clientId);
    @Event(forwardToParent = true)
    void getRootCategories();

//    @Event(forwardToParent = true)
//    void checkFreeEmail(String value);
    /**************************************************************************/
    /* Business events. */
    /* Business events handled by Presenters. */
    @Event(handlers = HomePresenter.class, historyConverter = HomeHistoryConverter.class)
    void displayMenu();

    /**
     * Assign widget to selected part and automatically removes previous widget. Optionally can remove widgets from
     * others anchors
     * TODO praso - rename this method to changeBody()
     * @param content
     */
    @DisplayChildModuleView({
        HomeSuppliersModule.class,
        HomeDemandsModule.class,
        DemandCreationModule.class,
        SupplierCreationModule.class })
    @Event(handlers = HomePresenter.class)
    void setBodyWidget(Widget content);

    @LoadChildModuleError
    @Event(handlers = HomePresenter.class)
    void errorOnLoad(Throwable reason);

    @BeforeLoadChildModule
    @Event(handlers = HomePresenter.class)
    void beforeLoad();

    @AfterLoadChildModule
    @Event(handlers = HomePresenter.class)
    void afterLoad();
//<<<<<<< .mine
//

    @Event(forwardToParent = true)
    void atAccount();

    /** demand creation related events. **/
//    @Event(handlers = FormLoginPresenter.class)
//    void initLoginForm(SimplePanel holderWidget);
//    @Event(handlers = DemandCreationPresenter.class)
//    void toggleLoginRegistration();
    // TODO praso - this should be moved somewhere else. Bud I don't know where :)
//    @Event(handlers = FormUserRegistrationPresenter.class, passive = true)
//    void checkFreeEmailResponse(Boolean result);
//    void checkFreeEmailResponse();
//    @Event(handlers = FormUserRegistrationPresenter.class)
//    void initRegistrationForm(SimplePanel holderWidget);
    //logic flow order representing registering client and then creating his demand
//    @Event(handlers = HomeHandler.class)
//    void registerNewClient(UserDetail newClient);
//    @Event(handlers = DemandCreationPresenter.class)
//    void prepareNewDemandForNewClient(UserDetail client);
    //alternative way of loging - verifying
//    @Event(handlers = HomeHandler.class)
//    void verifyExistingClient(UserDetail client);
    //error output
//    @Event(handlers = DemandCreationPresenter.class)
//    void loginError();
    /** Home category display widget and related call. */
    @Event(handlers = CategoryDisplayPresenter.class)
    void initCategoryDisplay(SimplePanel holderWidget);

    @Event(handlers = CategoryDisplayPresenter.class)
    void displayRootCategories(ArrayList<CategoryDetail> list);

    @Event(handlers = CategoryDisplayPresenter.class)
    void setCategoryDisplayData(ArrayList<CategoryDetail> list);

    /* Business events handled by Handlers. */
    /********* SEARCH PANEL **********************/
    @Event(handlers = HomePresenter.class)
    void showHideAdvancedSearchPanel(String content, int whereIdx, int catIdx, int locIdx);

    @Event(handlers = SearchHandler.class)
    void getCategories();

    @Event(handlers = SearchHandler.class)
    void getLocalities();

    @Event(handlers = HomePresenter.class)
    void setCategoryData(ArrayList<CategoryDetail> list);

    @Event(handlers = HomePresenter.class)
    void setLocalityData(ArrayList<LocalityDetail> list);
}