package cz.poptavka.sample.client.home;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.annotation.module.AfterLoadChildModule;
import com.mvp4g.client.annotation.module.BeforeLoadChildModule;
import com.mvp4g.client.annotation.module.ChildModule;
import com.mvp4g.client.annotation.module.ChildModules;
import com.mvp4g.client.annotation.module.DisplayChildModuleView;
import com.mvp4g.client.annotation.module.LoadChildModuleError;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.home.creation.DemandCreationPresenter;
import cz.poptavka.sample.client.home.creation.FormLoginPresenter;
import cz.poptavka.sample.client.home.creation.FormUserRegistrationPresenter;
import cz.poptavka.sample.client.home.supplier.SupplierCreationPresenter;
import cz.poptavka.sample.client.home.suppliers.SuppliersHandler;
import cz.poptavka.sample.client.home.suppliers.SuppliersPresenter;
import cz.poptavka.sample.client.home.widget.category.CategoryDisplayPresenter;
import cz.poptavka.sample.client.homedemands.HomeDemandsModule;
import cz.poptavka.sample.client.homesuppliers.HomeSuppliersModule;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;

@Events(startView = HomeView.class, module = HomeModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
@ChildModules({
    @ChildModule(moduleClass = HomeDemandsModule.class, async = true, autoDisplay = true),
    @ChildModule(moduleClass = HomeSuppliersModule.class, async = true, autoDisplay = true)
})
public interface HomeEventBus extends EventBus {

    @Start
    @Event(handlers = HomePresenter.class)
    void start();

    /** init method  **/
    @Event(handlers = HomePresenter.class, historyConverter = HomeHistoryConverter.class)
    String atHome();

    /** init method. **/
    @Event(handlers = HomePresenter.class, historyConverter = HomeHistoryConverter.class)
    void displayMenu();

    /** Display HomeView - parent Widget for public section. **/
    @Event(forwardToParent = true)
    void setBodyHolderWidget(Widget body);

    /**
     * Assign widget to selected part and automatically removes previous widget. Optionally can remove widgets from
     * others anchors
     * TODO praso - rename this method to changeBody()
     * @param content
     */
    @DisplayChildModuleView({ HomeSuppliersModule.class, HomeDemandsModule.class })
    @Event(handlers = HomePresenter.class)
    void setBodyWidget(Widget content);

    /** navigation events.  */
    @Event(handlers = DemandCreationPresenter.class, historyConverter = HomeHistoryConverter.class)
    String atCreateDemand();

    @Event(modulesToLoad = HomeDemandsModule.class)
    void goToHomeDemands();

//    @Event(handlers = ????.class, historyConverter = HomeHistoryConverter.class)
//    String atAttachement();
//    @Event(handlers = ????, historyConverter = HomeHistoryConverter.class)
//    String atLogin();
    // moved do HomeSuppliersModule
    //Display root categories
//    @Event(handlers = SuppliersPresenter.class)
//    String atSuppliers();

    /* Navigation event that initiates HomeSuppliersModule. */
    @Event(modulesToLoad = HomeSuppliersModule.class)
    void goToHomeSuppliers();

    // moved do HomeSuppliersModule
    //Display subcategories, suppliers of selected category and detail of selected supplier
    @Event(handlers = SuppliersPresenter.class)
    void atDisplaySuppliers(CategoryDetail categoryDetail);

//    @Event(handlers = RootPresenter.class, historyConverter = HomeHistoryConverter.class)
//    String createToken(String token);
    @Event(handlers = SupplierCreationPresenter.class, historyConverter = HomeHistoryConverter.class)
    String atRegisterSupplier();

    /** main module calls - common widgets */
    @Event(forwardToParent = true)
    void initDemandBasicForm(SimplePanel holderWidget);

    @Event(forwardToParent = true)
    void initCategoryWidget(SimplePanel holderWidget);

    @Event(forwardToParent = true)
    void initLocalityWidget(SimplePanel holderWidget);

    @Event(forwardToParent = true)
    void initDemandAdvForm(SimplePanel holderWidget);

    @Event(forwardToParent = true)
    void initServiceForm(SimplePanel serviceHolder);

    @Event(forwardToParent = true)
    void initSupplierForm(SimplePanel supplierInfoHolder);

    /** main module calls - Handler calls */
    @Event(forwardToParent = true)
    void createDemand(FullDemandDetail newDemand, Long clientId);

    @Event(forwardToParent = true)
    void getRootCategories();

    @Event(forwardToParent = true)
    void checkFreeEmail(String value);

    /** demand creation related events **/
    @Event(handlers = FormLoginPresenter.class)
    void initLoginForm(SimplePanel holderWidget);

    @Event(handlers = DemandCreationPresenter.class)
    void toggleLoginRegistration();

    @Event(handlers = FormUserRegistrationPresenter.class, passive = true)
    void checkFreeEmailResponse(Boolean result);
//    void checkFreeEmailResponse();

    @Event(handlers = FormUserRegistrationPresenter.class)
    void initRegistrationForm(SimplePanel holderWidget);
    //logic flow order representing registering client and then creating his demand

    @Event(handlers = HomeHandler.class)
    void registerNewClient(UserDetail newClient);

    @Event(handlers = DemandCreationPresenter.class)
    void prepareNewDemandForNewClient(UserDetail client);

    //alternative way of loging - verifying
    @Event(handlers = HomeHandler.class)
    void verifyExistingClient(UserDetail client);
    //error output

    @Event(handlers = DemandCreationPresenter.class)
    void loginError();

    /** Home category display widget and related call */
    @Event(handlers = CategoryDisplayPresenter.class)
    void initCategoryDisplay(SimplePanel holderWidget);

    @Event(handlers = CategoryDisplayPresenter.class)
    void displayRootCategories(ArrayList<CategoryDetail> list);

    @Event(handlers = CategoryDisplayPresenter.class)
    void setCategoryDisplayData(ArrayList<CategoryDetail> list);

    /** Supplier registration **/
    @Event(handlers = HomeHandler.class)
    void registerSupplier(UserDetail newSupplier);

    /** Common method calls **/
    /** DO NOT EDIT **/
    /** Method for setting public UI layout. */
    @Event(forwardToParent = true)
    void setPublicLayout();

    /** Popup methods for shoving, changing text and hiding,
     * for letting user know, that application is still working.
     * Every Child Module HAVE TO implement this method calls.
     * Popup methods for shoving, changing text and hiding, for letting user know, that application is still working.
     */
    @Event(forwardToParent = true)
    void loadingShow(String loadingMessage);

    @Event(forwardToParent = true)
    void loadingHide();

    /** NO METHODS AFTER THIS **/
    /** DISPLAT SUPPLIERS */
    //Category
    @Event(handlers = SuppliersHandler.class)
    void getSubCategories(Long category);

    @Event(handlers = SuppliersHandler.class)
    void getCategories();

    //Locality
    @Event(handlers = SuppliersHandler.class)
    void getLocalities();

    //Suppliers - wil be moved to HomeSuppliersModule
//    @Event(handlers = RootPresenter.class)
//    void getSuppliers(Long category, Long locality);
    @Event(handlers = SuppliersHandler.class)
    void getSuppliersByCategoryLocality(int start, int count, Long category, String locality);

    @Event(handlers = SuppliersHandler.class)
    void getSuppliersByCategory(int start, int count, Long category);

    @Event(handlers = SuppliersHandler.class)
    void getSuppliersCount(Long category, String locality);

    @Event(handlers = SuppliersHandler.class)
    void getSuppliersCountByCategory(Long category);

    @Event(handlers = SuppliersHandler.class)
    void getSuppliersCountByCategoryLocality(Long category, String locality);

    //Display
    @Event(handlers = SuppliersPresenter.class)
    void displayRootcategories(ArrayList<CategoryDetail> list);

    @Event(handlers = SuppliersPresenter.class)
    void displaySubCategories(ArrayList<CategoryDetail> list, Long parentCategory);

    @Event(handlers = SuppliersPresenter.class)
    void displaySuppliers(ArrayList<FullSupplierDetail> list);

    @Event(handlers = SuppliersPresenter.class)
    void setLocalityData(ArrayList<LocalityDetail> list);

    @Event(handlers = SuppliersPresenter.class, historyConverter = HomeHistoryConverter.class)
    void addToPath(CategoryDetail category);

    @Event(handlers = SuppliersPresenter.class)
    void removeFromPath(Long code);

    @Event(handlers = SuppliersPresenter.class)
    void setCategoryID(Long categoryCode);

    @Event(handlers = SuppliersPresenter.class)
    void resetDisplaySuppliersPager(int totalFoundNew);

    //DISPLAY DEMANDS - was moved to homedemands package

    /* Business events */
    @LoadChildModuleError
    @Event(handlers = HomePresenter.class)
    void errorOnLoad(Throwable reason);

    @BeforeLoadChildModule
    @Event(handlers = HomePresenter.class)
    void beforeLoad();

    @AfterLoadChildModule
    @Event(handlers = HomePresenter.class)
    void afterLoad();
}
